package com.revvo.data.model

data class Ride(
    val id: String,
    val title: String,
    val location: String,
    val date: String,
    val distance: String,
    val maxRiders: Int,
    val joinedRiders: Int = 0,
    val description: String = ""
)