package com.revvo.data.model

/**
 * Domain model for a User.
 *
 * For Phase 3 / Firebase Auth:
 *  - `id` will map to FirebaseAuth.uid
 *  - Don't store FirebaseUser objects in ViewModels — map to this domain User immediately.
 */
data class User(
    val id: String,
    val name: String,
    val bike: String,
    val location: String,
    val totalDistanceKm: Int,
    val totalRides: Int,
    val xp: Int
)
