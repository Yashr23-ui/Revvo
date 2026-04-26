package com.revvo.data.repository

import com.revvo.data.model.Ride
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for ride data.
 *
 * IMPORTANT: This is an INTERFACE so that we can swap the in-memory implementation for
 * a Firebase-backed one in Phase 3 without touching ViewModels or screens.
 *
 * - Reads are exposed as [Flow] so the UI re-renders automatically when data changes
 *   (whether from a local mutation or, later, a Firestore snapshot listener).
 * - Writes are `suspend` so the future Firebase implementation can perform real I/O
 *   on a background dispatcher.
 */
interface RideRepository {

    /** Stream of all rides. Emits a new list every time the underlying data changes. */
    fun observeRides(): Flow<List<Ride>>

    /** Snapshot of a single ride, or null if not found. Suspending so it can hit network later. */
    suspend fun getRideById(id: String): Ride?

    /** Stream of a single ride. Emits null if the ride disappears. */
    fun observeRideById(id: String): Flow<Ride?>

    /** Add a brand new ride. Returns the created ride (with generated id). */
    suspend fun addRide(ride: Ride): Ride

    /** Increment joinedRiders for the given ride if there's room. No-op if already full. */
    suspend fun joinRide(rideId: String)

    /** Decrement joinedRiders. No-op if already zero. */
    suspend fun leaveRide(rideId: String)
}
