package com.revvo.ui.model

import androidx.compose.runtime.Immutable
import com.revvo.data.model.RideStatus

/**
 * UI-shaped projection of [com.revvo.data.model.Ride].
 *
 * Why a separate class instead of using [com.revvo.data.model.Ride] directly in Compose:
 *   - Decouples UI from data (we can rename a field on Ride without touching screens).
 *   - Pre-formats values (e.g. "32 km" string) so screens stay dumb.
 *   - Marked @Immutable so Compose can skip recomposition when the instance is unchanged.
 */
@Immutable
data class RideCardData(
    val rideId: String,
    val title: String,
    val organizer: String,
    val date: String,
    val distance: String,
    val memberCount: Int,
    val maxMembers: Int,
    val status: RideStatus,
    val startLocation: String
)
