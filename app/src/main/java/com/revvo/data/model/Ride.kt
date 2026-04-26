package com.revvo.data.model

/**
 * Domain model for a Ride.
 *
 * Notes for Phase 3 / Firebase:
 *  - Keep this class free of any Firestore / Android dependencies. When Firebase is added,
 *    introduce a separate `RideDto` for serialization and map between the two.
 *  - `dateMillis` is the source of truth for sorting & "is this in the past?" logic.
 *    `displayDate` is purely for UI ("23 Feb"). Don't compute one from the other in the UI.
 */
data class Ride(
    val id: String,
    val title: String,
    val startLocation: String,
    val endLocation: String,
    val displayDate: String,
    val dateMillis: Long,
    val distanceKm: Int,
    val maxRiders: Int,
    val joinedRiders: Int = 0,
    val description: String = "",
    val hostId: String,
    val hostName: String,
    val status: RideStatus = RideStatus.UPCOMING
) {
    val isFull: Boolean get() = joinedRiders >= maxRiders
    val canJoin: Boolean get() = status == RideStatus.UPCOMING && !isFull
}

enum class RideStatus { UPCOMING, LIVE, COMPLETED, CANCELLED }
