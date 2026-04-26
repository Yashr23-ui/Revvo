package com.revvo.services.location

import kotlinx.coroutines.flow.Flow

/**
 * Live location stream contract — Phase 3 deliverable.
 *
 * Implementations will use FusedLocationProviderClient + a foreground service for
 * background tracking. Keep the interface tiny: the rest of the app should never need to
 * know whether updates come from GPS, fused, mock, etc.
 */
interface LocationService {

    data class LatLng(val lat: Double, val lng: Double, val timestampMs: Long)

    /** Cold flow — collecting starts location updates; cancelling stops them. */
    fun observeLocation(): Flow<LatLng>
}
