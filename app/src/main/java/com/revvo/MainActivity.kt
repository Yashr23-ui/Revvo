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
import com.revvo.ui.navigation.AppNavHost
import com.revvo.ui.theme.RevvoTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RevvoTheme {

                // ── Track which screen is currently showing ──────────────────
                var currentScreen  by remember { mutableStateOf("home") }
                var selectedRideId by remember { mutableStateOf("") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Show bottom nav only on main screens
                        if (currentScreen in listOf("home", "rides", "profile", "create")) {
                            BottomNavigationBar(
                                currentRoute   = currentScreen,
                                onItemSelected = { currentScreen = it }
                            )
                        }
                    }
                ) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost(
                            currentScreen = currentScreen,
                            selectedRideId = selectedRideId,
                            onCurrentScreenChange = { currentScreen = it },
                            onSelectedRideIdChange = { selectedRideId = it },
                            onEditProfile = { }
                        )

                    } // ← closes Box

                } // ← closes Scaffold content
            }
        }
    }
}