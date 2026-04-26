package com.revvo.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.revvo.RevvoApp
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel

/**
 * Single Factory that knows how to construct every ViewModel in the app from the
 * [com.revvo.di.AppContainer].
 *
 * Adding a new ViewModel = adding one `initializer { … }` block. No new files.
 *
 * If this file ever exceeds ~150 lines, that's the signal to introduce Hilt.
 */
object ViewModelFactory {

    val Factory: ViewModelProvider.Factory = viewModelFactory {

        initializer {
            val app = appFromExtras(this)
            RideViewModel(app.container.rideRepository)
        }

        initializer {
            val app = appFromExtras(this)
            UserViewModel(app.container.userRepository)
        }

        initializer {
            val app = appFromExtras(this)
            AuthViewModel(app.container.authRepository)
        }
    }

    private fun appFromExtras(extras: CreationExtras): RevvoApp {
        return checkNotNull(extras[APPLICATION_KEY]) {
            "Application is missing from CreationExtras. Are you calling viewModel() outside an Activity?"
        } as RevvoApp
    }
}

/**
 * Convenience wrapper so screens can write
 *
 *     val rideVm: RideViewModel = revvoViewModel()
 *
 * instead of repeating the factory argument everywhere.
 */
@Composable
inline fun <reified VM : ViewModel> revvoViewModel(): VM =
    composeViewModel(factory = ViewModelFactory.Factory)
