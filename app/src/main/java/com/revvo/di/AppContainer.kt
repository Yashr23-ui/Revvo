package com.revvo.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.revvo.data.repository.AuthRepository
import com.revvo.data.repository.FirebaseAuthRepository
import com.revvo.data.repository.FirebaseUserRepository
import com.revvo.data.repository.InMemoryRideRepository
import com.revvo.data.repository.RideRepository
import com.revvo.data.repository.UserRepository

/**
 * Manual dependency container.
 *
 * Holds the single instance of each repository for the app's lifetime. Injected into
 * ViewModels through [com.revvo.viewmodel.ViewModelFactory] — ViewModels NEVER construct
 * their own dependencies.
 *
 * Phase 2 (current): Auth is real Firebase. Ride/User repos are still InMemory — those
 * flip to Firebase in the next two passes.
 *
 * Why not Hilt? Two reasons:
 *  1. The team is still learning the architecture; one moving part at a time.
 *  2. With ~3 dependencies, manual wiring is shorter and more readable than Hilt boilerplate.
 *
 * When to switch to Hilt: when this file passes ~10 fields, or when you need scoped
 * dependencies (per-feature, per-screen).
 */
class AppContainer {
    // Firebase singletons — created lazily on first access.
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val firestore: FirebaseFirestore by lazy { Firebase.firestore }

    val authRepository: AuthRepository = FirebaseAuthRepository(firebaseAuth, firestore)

    val userRepository: UserRepository = FirebaseUserRepository(firestore, authRepository)

    // TODO Phase 2.3: replace with FirebaseRideRepository(firestore, authRepository)
    val rideRepository: RideRepository = InMemoryRideRepository()
}
