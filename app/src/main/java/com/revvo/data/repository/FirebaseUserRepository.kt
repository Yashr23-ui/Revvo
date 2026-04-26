package com.revvo.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.revvo.data.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

/**
 * Firestore-backed [UserRepository].
 *
 * Composes auth + firestore: whenever the auth state changes, we switch to observing the
 * matching `users/{uid}` document. When signed out, we emit null. This is the magic that
 * makes Profile screen automatically refresh when a user signs in/out without any
 * navigation or screen-level wiring.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseUserRepository(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : UserRepository {

    override fun observeCurrentUser(): Flow<User?> =
        authRepository.observeAuthState().flatMapLatest { state ->
            when (state) {
                is AuthState.LoggedIn -> observeUserDoc(state.uid)
                AuthState.LoggedOut, AuthState.Loading -> flowOf(null)
            }
        }

    private fun observeUserDoc(uid: String): Flow<User?> = callbackFlow {
        val ref = firestore.collection("users").document(uid)
        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                trySend(null)
                return@addSnapshotListener
            }
            trySend(snapshot.toUserOrNull())
        }
        awaitClose { listener.remove() }
    }

    override suspend fun getCurrentUser(): User? {
        val uid = authRepository.currentUserId() ?: return null
        return runCatching {
            firestore.collection("users").document(uid).get().await().toUserOrNull()
        }.getOrNull()
    }

    override suspend fun updateUser(user: User) {
        // Use update() rather than set() so we don't blow away createdAt or future fields.
        firestore.collection("users").document(user.id).update(
            mapOf(
                "name" to user.name,
                "bike" to user.bike,
                "location" to user.location,
                "totalDistanceKm" to user.totalDistanceKm,
                "totalRides" to user.totalRides,
                "xp" to user.xp
            )
        ).await()
    }

    /**
     * Map a Firestore doc to our domain [User]. Returns null if the doc doesn't exist —
     * caller handles that by showing an empty state.
     *
     * Note: numeric fields come back as Long from Firestore even when written as Int. Don't
     * try to `as Int` cast — convert explicitly.
     */
    private fun DocumentSnapshot.toUserOrNull(): User? {
        if (!exists()) return null
        return User(
            id = getString("id") ?: id,
            name = getString("name").orEmpty(),
            bike = getString("bike").orEmpty(),
            location = getString("location").orEmpty(),
            totalDistanceKm = (getLong("totalDistanceKm") ?: 0L).toInt(),
            totalRides = (getLong("totalRides") ?: 0L).toInt(),
            xp = (getLong("xp") ?: 0L).toInt()
        )
    }
}
