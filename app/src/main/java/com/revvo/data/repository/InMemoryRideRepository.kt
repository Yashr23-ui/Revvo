package com.revvo.data.repository

import com.revvo.data.model.Ride
import com.revvo.data.model.RideStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

/**
 * In-memory implementation of [RideRepository].
 *
 * Used during development before Firebase is wired up. State lives only in process memory —
 * it WILL be lost when the app is killed. This is intentional; Phase 3 swaps this out for
 * `FirebaseRideRepository` without changing any caller.
 *
 * Thread-safety: a [Mutex] guards mutations because writes can come from multiple
 * `viewModelScope` coroutines concurrently. Reads go through the [MutableStateFlow] which
 * is already safe for concurrent access.
 */
class InMemoryRideRepository : RideRepository {

    private val mutex = Mutex()

    private val seed = listOf(
        Ride(
            id = "ride-1",
            title = "Landour Ride",
            startLocation = "Dehradun",
            endLocation = "Landour",
            displayDate = "23 Feb",
            dateMillis = System.currentTimeMillis() + DAY_MS * 3,
            distanceKm = 18,
            maxRiders = 20,
            joinedRiders = 8,
            description = "Morning mountain ride to Landour.",
            hostId = "user-1",
            hostName = "Shashwat Negi",
            status = RideStatus.UPCOMING
        ),
        Ride(
            id = "ride-2",
            title = "Mussoorie Loop",
            startLocation = "Mussoorie",
            endLocation = "Kempty Falls",
            displayDate = "25 Feb",
            dateMillis = System.currentTimeMillis() + DAY_MS * 5,
            distanceKm = 32,
            maxRiders = 15,
            joinedRiders = 6,
            description = "Scenic group ride through Mussoorie loop.",
            hostId = "user-1",
            hostName = "Shashwat Negi",
            status = RideStatus.UPCOMING
        )
    )

    private val _rides = MutableStateFlow(seed)

    override fun observeRides(): Flow<List<Ride>> = _rides.asStateFlow()

    override suspend fun getRideById(id: String): Ride? =
        _rides.value.firstOrNull { it.id == id }

    override fun observeRideById(id: String): Flow<Ride?> =
        _rides.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun addRide(ride: Ride): Ride = mutex.withLock {
        val newRide = if (ride.id.isBlank()) ride.copy(id = UUID.randomUUID().toString()) else ride
        _rides.value = _rides.value + newRide
        newRide
    }

    override suspend fun joinRide(rideId: String) = mutex.withLock {
        _rides.value = _rides.value.map { ride ->
            if (ride.id != rideId || ride.isFull) ride
            else ride.copy(joinedRiders = (ride.joinedRiders + 1).coerceAtMost(ride.maxRiders))
        }
    }

    override suspend fun leaveRide(rideId: String) = mutex.withLock {
        _rides.value = _rides.value.map { ride ->
            if (ride.id != rideId || ride.joinedRiders <= 0) ride
            else ride.copy(joinedRiders = ride.joinedRiders - 1)
        }
    }

    private companion object {
        const val DAY_MS = 24 * 60 * 60 * 1000L
    }
}
