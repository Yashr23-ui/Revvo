package com.revvo.viewmodel

import androidx.lifecycle.ViewModel
import com.revvo.data.model.Ride
import com.revvo.data.repository.RideRepository
import com.revvo.ui.components.RideCardData
import com.revvo.ui.model.toRideCardData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class RideViewModel : ViewModel() {

    private val repository = RideRepository()

    private val _rides = MutableStateFlow<List<Ride>>(emptyList())
    val rides: StateFlow<List<Ride>> = _rides

    private val _rideCards = MutableStateFlow<List<RideCardData>>(emptyList())
    val rideCards: StateFlow<List<RideCardData>> = _rideCards

    init { loadRides() }

    private fun loadRides() {
        val rideList = repository.getRides()
        _rides.value = rideList
        _rideCards.value = rideList.map { it.toRideCardData() }
    }

    fun createRide(
        title: String,
        location: String,
        date: String,
        distance: String,
        maxRiders: Int,
        description: String = ""
    ) {
        val ride = Ride(
            id = UUID.randomUUID().toString(),
            title = title,
            location = location,
            date = date,
            distance = distance,
            maxRiders = maxRiders,
            joinedRiders = 0,
            description = description
        )

        repository.addRide(ride)
        loadRides()
    }

    fun getRideById(id: String): Ride? = repository.getRideById(id)

    fun joinRide(id: String) {
        repository.joinRide(id)
        loadRides()
    }

    fun leaveRide(id: String) {
        repository.leaveRide(id)
        loadRides()
    }
}