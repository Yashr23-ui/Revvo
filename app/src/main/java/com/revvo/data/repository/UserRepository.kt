package com.revvo.data.repository

import com.revvo.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for the currently signed-in user.
 *
 * Phase 3 will replace [InMemoryUserRepository] with a Firebase-backed implementation that
 * sources the user from FirebaseAuth + a Firestore /users/{uid} document. Callers shouldn't
 * need to change.
 */
interface UserRepository {

    /** Stream of the current user. Emits null when signed out (Phase 3). */
    fun observeCurrentUser(): Flow<User?>

    /** Snapshot. Returns null when signed out. */
    suspend fun getCurrentUser(): User?

    /** Update editable profile fields. */
    suspend fun updateUser(user: User)
}
