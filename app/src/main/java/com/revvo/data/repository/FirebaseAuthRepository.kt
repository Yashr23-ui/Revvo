package com.revvo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Firebase implementation of [AuthRepository].
 *
 * Uses [callbackFlow] to bridge Firebase's listener API into a cold Flow. The first
 * emission happens immediately when [FirebaseAuth.AuthStateListener] attaches — Firebase
 * fires it synchronously with the current user (or null), so consumers don't sit in
 * Loading forever.
 */
class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override fun observeAuthState(): Flow<AuthState> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            val state = if (user == null) {
                AuthState.LoggedOut
            } else {
                AuthState.LoggedIn(
                    uid = user.uid,
                    email = user.email,
                    displayName = user.displayName
                )
            }
            trySend(state)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun currentUserId(): String? = auth.currentUser?.uid

    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> =
        runCatching {
            auth.signInWithEmailAndPassword(email.trim(), password).await()
            Unit
        }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        name: String
    ): Result<Unit> = runCatching {
        val authResult = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val user = authResult.user ?: error("Firebase returned no user after sign-up")

        // Set displayName on the FirebaseAuth user so currentUser.displayName works.
        user.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name.trim())
                .build()
        ).await()

        // Seed the Firestore profile doc. Best-effort — if this fails the auth account
        // is still valid and we'll lazy-create the doc on first read.
        runCatching {
            firestore.collection("users").document(user.uid).set(
                mapOf(
                    "id" to user.uid,
                    "name" to name.trim(),
                    "bike" to "",
                    "location" to "",
                    "totalDistanceKm" to 0,
                    "totalRides" to 0,
                    "xp" to 0,
                    "createdAt" to System.currentTimeMillis()
                )
            ).await()
        }
        Unit
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        val user = authResult.user ?: error("Firebase returned no user after Google sign-in")

        // Lazy-create the user doc on first Google sign-in. Skipped if it already exists.
        val docRef = firestore.collection("users").document(user.uid)
        runCatching {
            val snapshot = docRef.get().await()
            if (!snapshot.exists()) {
                docRef.set(
                    mapOf(
                        "id" to user.uid,
                        "name" to (user.displayName ?: ""),
                        "bike" to "",
                        "location" to "",
                        "totalDistanceKm" to 0,
                        "totalRides" to 0,
                        "xp" to 0,
                        "createdAt" to System.currentTimeMillis()
                    )
                ).await()
            }
        }
        Unit
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}
