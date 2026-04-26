package com.revvo.ui.model

import com.revvo.data.model.Ride

/**
 * Pure mapping function: domain [Ride] -> UI [RideCardData].
 *
 * Pure & top-level so it's trivially unit-testable without instantiating anything.
 *
 * Note that we DO NOT derive [com.revvo.data.model.RideStatus] from `joinedRiders`/`maxRiders`
 * here — that was a bug in the previous mapper. "Ride is full" is shown via member count;
 * "Ride is completed" is a real lifecycle state on the domain model.
 */
fun Ride.toRideCardData(): RideCardData = RideCardData(
    rideId = id,
    title = title,
    organizer = hostName,
    date = displayDate,
    distance = "$distanceKm km",
    memberCount = joinedRiders,
    maxMembers = maxRiders,
    status = status,
    startLocation = startLocation
)
