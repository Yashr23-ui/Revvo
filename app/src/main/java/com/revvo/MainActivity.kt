package com.revvo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.revvo.data.repository.AuthState
import com.revvo.ui.components.BottomNavigationBar
import com.revvo.ui.navigation.AppNavHost
import com.revvo.ui.navigation.Routes
import com.revvo.ui.screens.AuthScreen
import com.revvo.ui.theme.RevvoDark
import com.revvo.ui.theme.RevvoOrange
import com.revvo.ui.theme.RevvoTheme
import com.revvo.viewmodel.AuthViewModel
import com.revvo.viewmodel.revvoViewModel

/**
 * Single Activity. Owns nothing but the auth gate, NavController, and Scaffold.
 *
 * The auth gate observes [AuthViewModel.authState] and decides which UI tree to show:
 *  - Loading  → splash (Firebase hasn't told us yet)
 *  - LoggedOut → AuthScreen
 *  - LoggedIn  → main nav graph
 *
 * Because the gate sits at the top of the tree, signing out anywhere in the app
 * automatically tears down the main nav and brings us back to AuthScreen — no manual
 * navigation needed.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RevvoTheme {
                AuthGate()
            }
        }
    }
}

@Composable
private fun AuthGate() {
    val authViewModel: AuthViewModel = revvoViewModel()
    val authState by authViewModel.authState.collectAsState()

    when (authState) {
        AuthState.Loading -> SplashScreen()
        AuthState.LoggedOut -> AuthScreen(authViewModel = authViewModel)
        is AuthState.LoggedIn -> RevvoAppRoot()
    }
}

@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(RevvoDark),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = RevvoOrange)
    }
}

@Composable
private fun RevvoAppRoot() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBarIfNeeded(navController = navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavHost(navController = navController)
        }
    }
}

/** Shows the bottom nav only on top-level routes. Detail screens hide it. */
@Composable
private fun BottomNavBarIfNeeded(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.HOME

    if (currentRoute in Routes.bottomNavRoutes) {
        BottomNavigationBar(
            currentRoute = currentRoute,
            onItemSelected = { route ->
                navController.navigate(route) {
                    // Pop everything up to the start destination so the back stack stays small.
                    popUpTo(Routes.HOME) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}
