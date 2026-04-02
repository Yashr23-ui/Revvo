package com.revvo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.revvo.ui.screens.CreateRideScreen
import com.revvo.ui.screens.HomeScreen
import com.revvo.ui.screens.ProfileScreen
import com.revvo.ui.screens.RideDetailsScreen
import com.revvo.viewmodel.RideViewModel
import com.revvo.viewmodel.UserViewModel

@Composable
fun AppNavHost(
    currentScreen: String,
    selectedRideId: String,
    onCurrentScreenChange: (String) -> Unit,
    onSelectedRideIdChange: (String) -> Unit,
    onEditProfile: () -> Unit
) {
    // Shared instances across screens for in-memory state.
    val rideViewModel: RideViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    when (currentScreen) {
        "home", "rides" -> {
            HomeScreen(
                onRideClick = { rideId ->
                    onSelectedRideIdChange(rideId)
                    onCurrentScreenChange("rideDetails")
                },
                onCreateRide = { onCurrentScreenChange("create") },
                rideViewModel = rideViewModel
            )
        }

        "create" -> {
            CreateRideScreen(
                onBack = { onCurrentScreenChange("home") },
                onRideCreated = { onCurrentScreenChange("home") },
                rideViewModel = rideViewModel
            )
        }

        "profile" -> {
            ProfileScreen(
                onEditProfile = onEditProfile,
                userViewModel = userViewModel
            )
        }

        "rideDetails" -> {
            RideDetailsScreen(
                rideId = selectedRideId,
                onBack = { onCurrentScreenChange("home") },
                onJoin = { onCurrentScreenChange("home") },
                rideViewModel = rideViewModel
            )
        }

        else -> {
            HomeScreen(
                onRideClick = { rideId ->
                    onSelectedRideIdChange(rideId)
                    onCurrentScreenChange("rideDetails")
                },
                onCreateRide = { onCurrentScreenChange("create") },
                rideViewModel = rideViewModel
            )
        }
    }
}

