# Revvo — Architecture & Team Handoff

> This document explains the state of the codebase as of the `refactor: clean MVVM architecture + Firebase auth + Firestore user repo` commit on `dev`. Read this before working on the codebase. AI assistants (Cursor, Copilot, Claude, etc.) should also be pointed at this file — it's structured for both humans and LLMs.

---

## 1. TL;DR

The codebase was rebuilt from a screen-with-hardcoded-data prototype into a proper layered MVVM Android app with Firebase. The architecture is now ready to absorb the rest of the product features (live ride tracking, maps, push notifications) without restructuring. Authentication and user profiles are real (Firebase Auth + Firestore). Rides are still in-memory pending the next swap. **If you have local work, save it elsewhere before pulling — the merge will be ugly.**

---

## 2. First-time setup after pulling this branch

Run in this order. Skipping steps will produce confusing errors.

### 2.1 Save anything in your working tree

If you have uncommitted work:

```bash
git stash
# or, copy your files outside the repo manually
```

Branches based on the old (pre-refactor) code are dead. Re-do that work on top of the new `dev`.

### 2.2 Pull

```bash
git checkout dev
git pull
```

### 2.3 Nuke IDE/build caches (mandatory — old caches reference the old structure)

Close Android Studio, then in File Explorer or terminal delete:

- `F:\Revvo\.idea\`
- `F:\Revvo\.gradle\`
- `F:\Revvo\build\`
- `F:\Revvo\app\build\`

Reopen the project in Android Studio.

### 2.4 Sync Gradle

When you reopen, you'll see a banner: **"Gradle files have changed since last project sync. A project sync may be necessary."** Click **Sync Now**.

First-time sync downloads Firebase + Navigation Compose + Credential Manager artifacts (~300–500 MB). Be patient.

### 2.5 You'll see a flood of red errors before Gradle finishes — ignore them

Don't open MainActivity.kt and panic about unresolved references during sync. Wait until the bottom status bar is idle.

### 2.6 Build → Rebuild Project

After sync. If errors persist, see [Common pitfalls](#10-common-pitfalls).

### 2.7 Get your debug SHA-1 added to Firebase (one-time per developer machine)

Required for Google Sign-In. In Android Studio:

1. Open the **Gradle** tool window (right edge, elephant icon).
2. `Revvo → Tasks → android → signingReport` — double-click.
3. In the Run output, find the `debug` variant, copy the `SHA1:` line.
4. Send it to the project owner. They'll register it in Firebase Console → Project Settings → Your apps → Add fingerprint.
5. Re-pull `app/google-services.json` (the owner will commit the updated file).

Until your SHA-1 is registered, Google Sign-In will fail with `ApiException: 10` on your device. Email/password auth works fine without this.

---

## 3. What changed (and why)

### 3.1 Before

- ViewModels constructed their own repositories (no DI, untestable, lifecycle-fragile).
- Repositories were concrete classes returning lists synchronously (no reactive flow).
- Navigation was manually managed with a `currentScreen` string and `selectedRideId` field on `MainActivity` (not back-stack-aware, no save state, no typed args).
- Models like `RideCardData` lived inside UI files (cross-layer leakage).
- No loading/error states — screens either had data or crashed.
- Form validation: none.
- Theme folder was capitalized (`ui/Theme/`) — works but breaks Linux casing conventions.

### 3.2 After

- **Manual DI** via `AppContainer` held by `RevvoApp` (Application class). Single line to swap any repository to a different implementation.
- **Repository pattern**: every data source is an interface + implementation. ViewModels see only interfaces.
- **Reactive data**: repositories return `Flow<T>`. ViewModels collect into `StateFlow<UiState<T>>` via `stateIn(SharingStarted.WhileSubscribed)`.
- **Navigation Compose** with typed routes in `Routes.kt`. `MainActivity` holds nothing but the `NavController` and `Scaffold`.
- **`UiState<T>` sealed interface**: every screen handles `Loading / Success / Error / Empty` explicitly.
- **`RideEvent` sealed interface + `SharedFlow`** for one-shot events (ride created, ride joined). State flows for state, shared flows for events.
- **`rememberSaveable`** form state in CreateRideScreen — survives rotation.
- **Firebase Auth** with sign-in/sign-up screen and an `AuthGate` at the top of the tree. Sign-out is wired.
- **Firestore-backed user profile** — Profile screen reads real data via `addSnapshotListener` wrapped in `callbackFlow`.

---

## 4. Project structure

```
app/src/main/java/com/revvo/
├── RevvoApp.kt                  ← Application class. Holds AppContainer.
├── MainActivity.kt              ← Hosts AuthGate. Decides login screen vs main app.
│
├── data/
│   ├── model/                   ← Plain domain models (no Compose, no Firebase types here).
│   │   ├── Ride.kt              ← Domain Ride. Has computed isFull, canJoin properties.
│   │   ├── RideStatus.kt
│   │   └── User.kt
│   │
│   └── repository/              ← Data layer. Interfaces + implementations.
│       ├── AuthRepository.kt    ← Interface + AuthState sealed type.
│       ├── FirebaseAuthRepository.kt
│       ├── UserRepository.kt    ← Interface.
│       ├── FirebaseUserRepository.kt
│       ├── InMemoryUserRepository.kt   ← Legacy, kept for reference. AppContainer no longer uses it.
│       ├── RideRepository.kt    ← Interface.
│       └── InMemoryRideRepository.kt   ← STILL ACTIVE. Replace with Firebase impl next.
│
├── di/
│   └── AppContainer.kt          ← Manual DI container. Swap implementations here.
│
├── services/
│   └── location/
│       └── LocationService.kt   ← Stub. To be implemented in Phase 3 (FusedLocationProvider).
│
├── ui/
│   ├── components/              ← Reusable Composables.
│   │   ├── BottomNavigationBar.kt
│   │   ├── RideCard.kt
│   │   ├── StatsCard.kt
│   │   ├── WaypointItem.kt
│   │   └── RevvoAnimations.kt
│   │
│   ├── model/                   ← UI-only models (presentation-shaped).
│   │   ├── RideCardData.kt      ← @Immutable. Mapped from domain Ride.
│   │   └── RideUiMapper.kt
│   │
│   ├── navigation/
│   │   ├── Routes.kt            ← All route strings as constants.
│   │   └── AppNavHost.kt        ← The single nav graph.
│   │
│   ├── screens/                 ← One file per screen. Composables only — no business logic.
│   │   ├── AuthScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── RidesScreen.kt
│   │   ├── CreateRideScreen.kt
│   │   ├── ProfileScreen.kt
│   │   └── RideDetailsScreen.kt
│   │
│   ├── state/
│   │   └── UiState.kt           ← Sealed: Loading / Success<T> / Error / Empty.
│   │
│   └── theme/                   ← Colors, typography, theme.
│
└── viewmodel/                   ← One file per ViewModel.
    ├── AuthViewModel.kt
    ├── RideViewModel.kt
    ├── UserViewModel.kt
    └── ViewModelFactory.kt      ← Single factory + revvoViewModel() helper.
```

### Layering rule

Imports go in one direction only:

```
ui (screens, components)  →  viewmodel  →  data.repository (interfaces)
                                           ↓
                                      data.model
```

Screens never import Firebase. ViewModels never import Firebase. Only files in `data/repository/` whose name starts with `Firebase` may import the Firebase SDK.

If you find yourself wanting to import Firestore from a screen, stop and put the logic in a repository.

---

## 5. Key patterns

### 5.1 UiState

Every screen that loads data observes a `StateFlow<UiState<T>>` and renders one of four cases:

```kotlin
when (val s = state) {
    is UiState.Loading -> CenteredProgress()
    is UiState.Empty   -> CenteredMessage("Nothing here yet")
    is UiState.Error   -> CenteredMessage(s.message)
    is UiState.Success -> ProfileContent(user = s.data)
}
```

Don't shortcut this. If a screen needs data and you find yourself writing `if (data != null)`, you're missing the loading state.

### 5.2 Repository pattern

Repositories are **interfaces** in `data/repository/`. Implementations sit alongside, prefixed with their backend (`Firebase…`, `InMemory…`).

ViewModels take the interface in their constructor:

```kotlin
class UserViewModel(
    private val userRepository: UserRepository  // ← interface, not impl
) : ViewModel()
```

This means you can swap implementations in `AppContainer.kt` without touching any ViewModel or screen. That's how Phase 2.2 (FirebaseUserRepository) shipped: zero changes outside `data/` and `di/`.

### 5.3 ViewModel + StateFlow

Standard pattern for exposing data:

```kotlin
val userState: StateFlow<UiState<User>> = userRepository.observeCurrentUser()
    .map { user -> if (user == null) UiState.Empty else UiState.Success(user) }
    .catch { emit(UiState.Error(it.message ?: "Unknown error")) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
```

`WhileSubscribed(5_000)` keeps the upstream flow active for 5 seconds after the last collector disconnects — handles configuration changes (rotation) without re-fetching, but releases resources when the screen is genuinely gone.

For one-shot events (navigate after success, show snackbar, etc.) use a `MutableSharedFlow`, not a state flow. State flows replay the latest value to new subscribers, which would re-trigger navigation.

### 5.4 Navigation

Routes are constants in `Routes.kt`:

```kotlin
object Routes {
    const val HOME = "home"
    const val RIDE_DETAILS_ARG = "rideId"
    const val RIDE_DETAILS = "ride_details/{$RIDE_DETAILS_ARG}"

    fun rideDetails(rideId: String) = "ride_details/$rideId"
}
```

The graph is in `AppNavHost.kt`. To add a route:

1. Add the constant to `Routes.kt`.
2. Add a `composable(Routes.NEW) { … }` block to `AppNavHost`.
3. Navigate via `navController.navigate(Routes.NEW)`.

Bottom nav uses `popUpTo(Routes.HOME) { saveState = true } / launchSingleTop = true / restoreState = true` so tab state survives switching.

### 5.5 Manual DI via AppContainer

```kotlin
// di/AppContainer.kt
class AppContainer {
    private val firebaseAuth by lazy { Firebase.auth }
    private val firestore   by lazy { Firebase.firestore }

    val authRepository: AuthRepository = FirebaseAuthRepository(firebaseAuth, firestore)
    val userRepository: UserRepository = FirebaseUserRepository(firestore, authRepository)
    val rideRepository: RideRepository = InMemoryRideRepository()  // ← swap me
}
```

That's the entire DI graph. ViewModels are constructed in `ViewModelFactory.kt` from this container.

When this file passes ~10 fields, that's the cue to migrate to Hilt. Until then, this is shorter and clearer.

### 5.6 Reactive composition with `flatMapLatest`

The Firestore-user pattern is worth understanding because we'll repeat it for rides:

```kotlin
override fun observeCurrentUser(): Flow<User?> =
    authRepository.observeAuthState().flatMapLatest { state ->
        when (state) {
            is AuthState.LoggedIn -> observeUserDoc(state.uid)
            else -> flowOf(null)
        }
    }
```

When auth state changes, `flatMapLatest` cancels the previous Firestore listener and starts a new one for the new user. This is how the Profile screen automatically refreshes on sign-in/sign-out without any manual hook.

---

## 6. Firebase

### 6.1 Project info

- Project ID: `revvo-4f68e`
- Firestore region: `asia-south1` (Mumbai)
- Auth providers enabled: Email/Password, Google (Google requires SHA-1 fingerprint registration per developer machine — see §2.7)

### 6.2 Firestore data model

```
/users/{uid}
    id: string                 ← matches doc ID, redundant for convenience
    name: string
    bike: string
    location: string
    totalDistanceKm: number    ← stored as Long, cast to Int in app
    totalRides: number
    xp: number
    createdAt: number          ← millis since epoch
```

Documents are created on sign-up by `FirebaseAuthRepository.signUpWithEmail`. Google sign-in lazy-creates on first sign-in.

### 6.3 Security rules — IMPORTANT

The database is currently in **test mode**: rules allow read/write from anyone until 30 days after creation. **Do not share the project ID publicly** until proper rules are deployed.

Tightened rules (write these in Firebase Console → Firestore → Rules) should look roughly like:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{uid} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == uid;
    }
    match /rides/{rideId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null && request.auth.uid == request.resource.data.hostId;
      allow update: if request.auth != null;  // tighten further when join logic stabilizes
      allow delete: if request.auth != null && request.auth.uid == resource.data.hostId;
    }
  }
}
```

These haven't been deployed yet. Coordinate with the project owner before pushing rules.

### 6.4 google-services.json

`app/google-services.json` is committed to the repo. The API key inside is restricted by package name + SHA-1 — not actually sensitive. Don't add it to `.gitignore`; teammates need it to build.

If you're forking this for a public repo, regenerate the file with restricted credentials before publishing.

---

## 7. How to add a new feature

This is the canonical flow. Follow it for any new screen/feature:

1. **Domain model** in `data/model/` if needed. Plain data class. No Compose, no Firebase types.
2. **Repository interface** in `data/repository/`. Methods return `Flow<T>` for streams, `suspend fun … : Result<T>` for actions.
3. **Repository implementation** (`InMemory…` for prototyping, `Firebase…` for real). Add to `AppContainer`.
4. **ViewModel** in `viewmodel/`. Constructor takes the repository interface. Expose `StateFlow<UiState<T>>` for state, `SharedFlow<Event>` for events.
5. **Register the ViewModel** in `ViewModelFactory.kt` with one `initializer { … }` block.
6. **Screen** in `ui/screens/`. Receive ViewModel as parameter. `collectAsState()` the state, `when` over UiState, render.
7. **Route** in `Routes.kt`, add to `AppNavHost.kt`.
8. **Navigation calls** — pass `onClick` lambdas down from the NavHost, never call NavController inside screens.

If any step feels redundant for a tiny feature, do it anyway — the pattern is what keeps the codebase scalable.

---

## 8. What's still pending

In priority order:

1. **`FirebaseRideRepository`** — biggest remaining unlock. Rides currently reset on every restart. Mirror the user-repo pattern: observe `rides` collection, `arrayUnion`/`arrayRemove` on `riderIds` for join/leave. (Phase 2.3)
2. **Edit Profile screen** — `Tune` icon on Profile is wired to a no-op. Need a screen to edit name/bike/location, then `userRepository.updateUser(…)`.
3. **Google Sign-In completion** — `AuthScreen` has the button but it's disabled. Wire Credential Manager flow → get ID token → `authRepository.signInWithGoogle(idToken)`. Already implemented in the repository, just needs UI.
4. **Firestore security rules** — deploy the rules in §6.3 before sharing more widely.
5. **Phase 3 — Location & Maps**:
   - Real `LocationService` impl using FusedLocationProviderClient inside a foreground service.
   - Map screen using `com.google.maps.android:maps-compose`.
   - Live ride tracking (start/pause/stop, polyline, distance accumulation).
6. **Phase 4 — Real ride features**:
   - "Last Ride Summary" on Home reads the user's most recent completed ride (currently shows em-dashes).
   - XP awarded on ride completion.
   - Ride chat/comments (optional, depends on product direction).
7. **Polish**: push notifications via FCM, image upload via Firebase Storage (replacing hardcoded Unsplash URLs), pull-to-refresh on Rides, search/filter rides.
8. **Tests**: `kotlinx-coroutines-test` is in the build but no tests yet. Start with ViewModel unit tests using a fake repository.
9. **Theme folder casing**: `ui/Theme/` should be `ui/theme/`. Refactor → Rename in Android Studio.

---

## 9. Conventions

### 9.1 Naming

- Repositories: `XxxRepository` (interface), `FirebaseXxxRepository` / `InMemoryXxxRepository` (impl).
- ViewModels: `XxxViewModel`.
- Screens: `XxxScreen` (composable).
- UI-only models: `XxxData` or `XxxUiState`.
- Sealed interface members: `data object` for stateless cases, `data class` for parameterized.

### 9.2 Imports

Use Android Studio's "Optimize Imports" before committing (Ctrl+Alt+O / Cmd+Opt+O). No wildcard imports.

### 9.3 ViewModels

- Always inject the repository **interface**, never the implementation.
- Always use `viewModelScope` for coroutines, never `GlobalScope` or a custom scope.
- Always wrap repository flows in `stateIn` before exposing.
- Never put Compose imports in a ViewModel.

### 9.4 Screens

- Take ViewModels and lambdas as parameters. Don't call `revvoViewModel()` deep inside screen sub-composables — only at the screen entry point or in `AppNavHost`.
- Don't call `NavController` from inside a screen — receive `onSomething` lambdas from the NavHost and let the NavHost handle navigation.
- `rememberSaveable` for any user-typed input, `remember` for ephemeral state.

### 9.5 Coroutines + Flow

- Repositories returning streams: `fun observeXxx(): Flow<…>`, never `suspend`.
- Repositories returning one-shot results: `suspend fun xxx(): Result<…>`, never `Flow`.
- ViewModels: always `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue)`.
- Don't use `collectAsState()` outside Composables.

---

## 10. Common pitfalls

### 10.1 "Unresolved reference" floods after pulling

Cause: Gradle hasn't synced yet, or IDE caches reference the old structure.

Fix: Sync Gradle. If still broken after sync, `File → Invalidate Caches and Restart` (check all four boxes).

### 10.2 Google Sign-In fails with `ApiException: 10`

Cause: your debug keystore SHA-1 isn't registered with Firebase.

Fix: §2.7. Each developer needs to do this once.

### 10.3 Sign-up succeeds but Profile briefly shows "Sign in to see your profile"

Cause: `FirebaseAuth` fires its state listener before the Firestore user doc finishes writing. Brief race condition.

Fix: not yet — fixable later by emitting `LoggedIn` only after the user doc write succeeds. Acceptable cosmetic glitch for now.

### 10.4 `popUpTo / saveState` errors in MainActivity

Cause: `saveState = true` parsed as `NavOptionsBuilder.saveState` (private) instead of `PopUpToBuilder.saveState`. Almost always a downstream effect of `Routes.HOME` being unresolved (which makes the parser misattribute).

Fix: resolve the underlying `Routes` import (Gradle sync, file exists at `ui/navigation/Routes.kt`).

### 10.5 "Constructor() conflicting overloads: RevvoApp"

Cause: there used to be both `class RevvoApp : Application` and `@Composable fun RevvoApp()`. Already resolved — the composable was renamed to `RevvoAppRoot`. If you see this in a new context, rename the composable.

### 10.6 Firestore field comes back as Long, not Int

Always: `(snapshot.getLong("xp") ?: 0L).toInt()`. Never `as Int`. Firestore stores all numbers as 64-bit.

### 10.7 "Disk space low" from Android Studio mid-build

Cause: Gradle caches in `~/.gradle/caches` plus build outputs. Firebase deps add ~300 MB.

Fix: `Build → Clean Project`. If chronic, move Gradle cache to a different drive (settings → Build, Execution, Deployment → Build Tools → Gradle → Service directory path).

---

## 11. Glossary (for new team members and AI assistants)

- **`AppContainer`** — Manual DI container. Holds all repository instances. One per app, lives on the Application.
- **`AuthGate`** — Composable in `MainActivity.kt` that observes `AuthViewModel.authState` and renders either `AuthScreen` (logged out) or `RevvoAppRoot` (logged in).
- **`AuthState`** — Sealed: `Loading` (Firebase hasn't reported yet) / `LoggedOut` / `LoggedIn(uid, email, displayName)`.
- **`callbackFlow`** — Coroutine bridge that turns a callback-style API (Firebase listeners, Android sensors, etc.) into a cold `Flow`. Used in `FirebaseAuthRepository.observeAuthState` and `FirebaseUserRepository.observeUserDoc`.
- **`Cold flow`** — A flow that does no work until someone collects it. The opposite is a hot flow (e.g., `StateFlow`, `SharedFlow`) which can have values without subscribers.
- **`flatMapLatest`** — Flow operator that switches to a new flow when the upstream emits, cancelling the previous one. Used to compose auth state with Firestore subscriptions.
- **`Manual DI`** — Wiring dependencies by hand in `AppContainer` instead of using a framework like Hilt or Koin. Fine for small apps; transition to Hilt around ~10 dependencies.
- **`MVVM`** — Model-View-ViewModel. View (Composable screen) observes ViewModel state. ViewModel coordinates Repositories. Repositories know about data sources.
- **`Repository`** — Owns a domain area's data. Returns `Flow<T>` for observable state, `suspend fun` for one-shot actions. The only place that talks to data sources (Firebase, Room, network).
- **`revvoViewModel()`** — Composable helper in `viewmodel/ViewModelFactory.kt`. Use this to obtain a ViewModel inside a screen instead of passing the factory manually.
- **`Routes`** — Object in `ui/navigation/Routes.kt` containing all navigation route strings as constants.
- **`SharedFlow`** — Hot flow for one-shot events (navigation triggers, snackbars). Doesn't replay state to new subscribers like `StateFlow` does.
- **`StateFlow`** — Hot flow that always has a current value, replays it to new subscribers. Use for screen state.
- **`stateIn`** — Operator that converts a cold flow into a `StateFlow`. Pair with `SharingStarted.WhileSubscribed(5_000)` for proper lifecycle handling.
- **`UiState<T>`** — Sealed interface in `ui/state/UiState.kt`: `Loading` / `Success<T>` / `Error` / `Empty`. Every data-loading screen renders one of these four.
- **`ViewModelFactory`** — Singleton in `viewmodel/ViewModelFactory.kt`. Produces every ViewModel from `AppContainer`. Adding a new ViewModel = one `initializer { … }` block.

---

## 12. Where to ask

- Architecture / "where does X go": this doc, then ping the owner.
- Firebase console access / SHA-1 registration: ping the project owner.
- Build broken after pull and §10.1 didn't fix it: paste the full error in the team channel.
- "Should I use a `StateFlow` or `SharedFlow`?": state → StateFlow, events → SharedFlow.

---

*Last updated: 2026-04-26 — at the `refactor: clean MVVM + Firebase auth + Firestore user repo` commit.*
