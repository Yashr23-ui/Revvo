package com.revvo.ui.state

/**
 * One-of-four states every async screen will be in.
 *
 * Use a sealed interface so `when` over it is exhaustive — the compiler forces every screen
 * to handle Loading and Error, which is exactly what stops the "blank screen for half a second"
 * bug class once Firebase is in.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String, val cause: Throwable? = null) : UiState<Nothing>
    data object Empty : UiState<Nothing>
}
