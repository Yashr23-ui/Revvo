package com.revvo.ui.model

import com.revvo.data.model.Ride
import com.revvo.ui.components.RideCardData
import com.revvo.ui.components.RideStatus

fun Ride.toRideCardData(): RideCardData {
    return RideCardData(
        rideId = id,
        title = title,
        organizer = "Revvo Rider",
        date = date,
        distance = distance,
        memberCount = joinedRiders,
        maxMembers = maxRiders,
        status = if (joinedRiders >= maxRiders) RideStatus.COMPLETED else RideStatus.UPCOMING,
        startLocation = location
    )
}
