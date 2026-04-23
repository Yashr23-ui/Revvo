package com.revvo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.revvo.ui.components.BottomNavigationBar
import com.revvo.ui.screens.*
import com.revvo.ui.theme.RevvoTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RevvoTheme {

                // ── Track which screen is currently showing ──────────────────
                var currentScreen  by remember { mutableStateOf("home") }
                var selectedRideId by remember { mutableStateOf("DEFAULT_RIDE") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Show bottom nav only on main screens
                        if (currentScreen in listOf("home", "profile", "create", "rides")) {
                            BottomNavigationBar(
                                currentRoute   = currentScreen,
                                onItemSelected = { currentScreen = it }
                            )
                        }
                    }
                ) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {

                        // ── Screen router ────────────────────────────────────
                        when (currentScreen) {

                            "home" -> HomeScreen(
                                onRideClick  = { rideId ->
                                    selectedRideId = rideId
                                    currentScreen  = "rides"
                                },
                                onCreateRide = { currentScreen = "create" }
                            )

                            "create" -> CreateRideScreen(
                                onBack        = { currentScreen = "home" },
                                onRideCreated = { title, start, dest, time, max, desc ->
                                    // Here you would normally call your ViewModel
                                    // rideViewModel.createRide(title, start, dest, time, max, desc)
                                    currentScreen = "home"
                                }
                            )

                            "profile" -> ProfileScreen(
                                onEditProfile = { }
                            )

                            "rides" -> RideDetailsScreen(
                                rideId = selectedRideId,
                                onBack = { currentScreen = "home" },
                                onJoin = { currentScreen = "home" }
                            )

                            // fallback
                            else -> HomeScreen(
                                onRideClick  = { rideId ->
                                    selectedRideId = rideId
                                    currentScreen  = "rides"
                                },
                                onCreateRide = { currentScreen = "create" }
                            )
                        }

                    } // ← closes Box

                } // ← closes Scaffold content
            }
        }
    }
}