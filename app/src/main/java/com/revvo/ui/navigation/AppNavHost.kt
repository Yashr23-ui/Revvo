package com.revvo.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.revvo.ui.screens.CreateRideScreen
import com.revvo.ui.screens.HomeScreen
import com.revvo.ui.screens.ProfileScreen
import com.revvo.ui.screens.RideDetailsScreen
import com.revvo.ui.screens.RidesScreen
import com.revvo.ui.theme.RevvoDark
import com.revvo.ui.theme.RevvoWhite
import com.revvo.viewmodel.AuthViewModel
import com.revvo.viewmodel.RideViewModel
import com.revvo.viewmodel.UserViewModel
import com.revvo.viewmodel.revvoViewModel

/**
 * The single nav graph for the app.
 *
 * Notes:
 *  - All ViewModels are obtained via [revvoViewModel] (which uses [com.revvo.viewmodel.ViewModelFactory]).
 *  - We hoist `RideViewModel` to the activity scope using `viewModel(LocalContext.current as ComponentActivity)`-equivalent
 *    by retrieving it once at the top level. That way Home / Rides / Details all see the
 *    same in-memory state. (Phase 3 will replace this with hilt-shared scoping.)
 *  - Detail route takes `rideId` as a typed nav argument — no more shared MainActivity state.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Activity-scoped VMs so all screens see the same data layer.
    val rideViewModel: RideViewModel = revvoViewModel()
    val userViewModel: UserViewModel = revvoViewModel()
    val authViewModel: AuthViewModel = revvoViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {

        composable(Routes.HOME) {
            HomeScreen(
                rideViewModel = rideViewModel,
                userViewModel = userViewModel,
                onRideClick = { rideId -> navController.navigate(Routes.rideDetails(rideId)) },
                onCreateRide = { navController.navigate(Routes.CREATE_RIDE) }
            )
        }

        composable(Routes.RIDES) {
            RidesScreen(
                rideViewModel = rideViewModel,
                onRideClick = { rideId -> navController.navigate(Routes.rideDetails(rideId)) }
            )
        }

        composable(Routes.CREATE_RIDE) {
            CreateRideScreen(
                rideViewModel = rideViewModel,
                onBack = { navController.popBackStack() },
                onRideCreated = { rideId ->
                    // Pop create off the stack, then go to details.
                    navController.popBackStack()
                    navController.navigate(Routes.rideDetails(rideId))
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                userViewModel = userViewModel,
                onEditProfile = { /* TODO: navController.navigate(Routes.EDIT_PROFILE) */ },
                onSignOut = { authViewModel.signOut() }
            )
        }

        composable(Routes.MAP) {
            // Placeholder — full map screen is a Phase 3 deliverable.
            PlaceholderScreen(label = "MAP — coming in Phase 3")
        }

        composable(
            route = Routes.RIDE_DETAILS,
            arguments = listOf(navArgument(Routes.RIDE_DETAILS_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString(Routes.RIDE_DETAILS_ARG).orEmpty()
            RideDetailsScreen(
                rideId = rideId,
                rideViewModel = rideViewModel,
                onBack = { navController.popBackStack() },
                onJoined = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(label: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(RevvoDark),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = RevvoWhite, style = MaterialTheme.typography.headlineMedium)
    }
}
