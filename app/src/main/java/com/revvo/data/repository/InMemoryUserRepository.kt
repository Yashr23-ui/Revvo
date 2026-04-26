package com.revvo.data.repository

import com.revvo.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory implementation of [UserRepository]. Replaced by `FirebaseUserRepository` in Phase 3.
 */
class InMemoryUserRepository : UserRepository {

    private val _user = MutableStateFlow<User?>(
        User(
            id = "user-1",
            name = "Shashwat Negi",
            bike = "RE Himalayan · 2022",
            location = "Dehradun, Uttarakhand",
            totalDistanceKm = 1200,
            totalRides = 24,
            xp = 3100
        )
    )

    override fun observeCurrentUser(): Flow<User?> = _user.asStateFlow()

    override suspend fun getCurrentUser(): User? = _user.value

    override suspend fun updateUser(user: User) {
        _user.value = user
    }
}
