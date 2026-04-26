package com.revvo.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * Auth state — the gate between "show login" and "show app".
 *
 * `Loading` is the initial state before Firebase has told us whether a user is signed in.
 * Without this, the UI flickers from logged-out → logged-in on every cold start.
 */
sealed interface AuthState {
    data object Loading : AuthState
    data object LoggedOut : AuthState
    data class LoggedIn(
        val uid: String,
        val email: String?,
        val displayName: String?
    ) : AuthState
}

/**
 * Auth contract.
 *
 * The repository is the only place in the app that talks to FirebaseAuth. ViewModels and
 * screens see this interface, never the SDK directly. That way swapping providers (or
 * stubbing for tests) doesn't ripple through the codebase.
 */
interface AuthRepository {

    /** Cold flow that emits the current auth state and any subsequent changes. */
    fun observeAuthState(): Flow<AuthState>

    /** Currently signed-in user's UID, or null. Use sparingly — prefer the flow. */
    fun currentUserId(): String?

    suspend fun signInWithEmail(email: String, password: String): Result<Unit>

    /**
     * Creates the FirebaseAuth account AND the corresponding `users/{uid}` Firestore doc
     * in one shot. If the doc write fails, the auth account is still created — that's
     * fine; we'll lazy-create the doc on first read in [com.revvo.data.repository.FirebaseUserRepository].
     */
    suspend fun signUpWithEmail(email: String, password: String, name: String): Result<Unit>

    /**
     * Sign in with a Google ID token obtained via the Credential Manager flow. The screen
     * is responsible for getting the token; the repository just exchanges it for a Firebase
     * session.
     */
    suspend fun signInWithGoogle(idToken: String): Result<Unit>

    suspend fun signOut()
}
