package com.revvo.data.repository

import com.revvo.data.model.User

class UserRepository {

    private var user = User(
        id = "1",
        name = "Rider",
        bike = "KTM Duke 390",
        totalDistance = 520,
        totalRides = 6,
        exp = 1500
    )

    fun getUser(): User {
        return user
    }

    fun updateUser(updatedUser: User) {
        user = updatedUser
    }
}