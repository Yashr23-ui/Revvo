package com.revvo.data.repository

import com.revvo.data.model.Ride

class RideRepository {

    private val rides = mutableListOf(
        Ride(
            id = "1",
            title = "Landour Ride",
            location = "Dehradun",
            date = "23 Feb",
            distance = "18 km",
            maxRiders = 20,
            joinedRiders = 8,
            description = "Morning mountain ride to Landour."
        ),
        Ride(
            id = "2",
            title = "Mussoorie Loop",
            location = "Mussoorie",
            date = "25 Feb",
            distance = "32 km",
            maxRiders = 15,
            joinedRiders = 6,
            description = "Scenic group ride through Mussoorie loop."
        )
    )

    fun getRides(): List<Ride> {
        return rides
    }

    fun addRide(ride: Ride) {
        rides.add(ride)
    }

    fun joinRide(id: String) {
        val index = rides.indexOfFirst { it.id == id }
        if (index == -1) return

        val ride = rides[index]
        val max = ride.maxRiders.coerceAtLeast(0)
        val current = ride.joinedRiders.coerceIn(0, max)
        val updatedJoined = (current + 1).coerceAtMost(max)

        if (updatedJoined == current) return
        rides[index] = ride.copy(joinedRiders = updatedJoined)
    }

    fun leaveRide(id: String) {
        val index = rides.indexOfFirst { it.id == id }
        if (index == -1) return

        val ride = rides[index]
        val current = ride.joinedRiders.coerceAtLeast(0)
        val updatedJoined = (current - 1).coerceAtLeast(0)

        if (updatedJoined == current) return
        rides[index] = ride.copy(joinedRiders = updatedJoined)
    }

    fun getRideById(id: String): Ride? {
        return rides.find { it.id == id }
    }
}