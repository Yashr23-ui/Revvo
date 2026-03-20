package com.revvo.data.model

data class User(
    val id: String,
    val name: String,
    val bike: String,
    val totalDistance: Int,
    val totalRides: Int,
    val exp: Int
)