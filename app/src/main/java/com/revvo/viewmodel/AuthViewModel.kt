package com.revvo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revvo.data.repository.AuthRepository
import com.revvo.data.repository.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Single ViewModel for both sign-in and sign-up.
 *
 * - [authState] is the source of truth for "are we logged in?" — MainActivity observes it
 *   and decides whether to render the auth screen or the main nav.
 * - [formState] tracks the in-flight submit so the screen can show a spinner / error
 *   without keeping its own boolean.
 */
class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val authState: StateFlow<AuthState> = authRepository.observeAuthState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly, // Eager — we need this before the first frame
            initialValue = AuthState.Loading
        )

    private val _formState = MutableStateFlow<AuthFormState>(AuthFormState.Idle)
    val formState: StateFlow<AuthFormState> = _formState.asStateFlow()

    fun signIn(email: String, password: String) {
        if (!validate(email, password)) return
        _formState.value = AuthFormState.Submitting
        viewModelScope.launch {
            authRepository.signInWithEmail(email, password)
                .onSuccess { _formState.value = AuthFormState.Idle }
                .onFailure { _formState.value = AuthFormState.Error(humanizeAuthError(it)) }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        if (name.isBlank()) {
            _formState.value = AuthFormState.Error("Please enter your name")
            return
        }
        if (!validate(email, password)) return
        _formState.value = AuthFormState.Submitting
        viewModelScope.launch {
            authRepository.signUpWithEmail(email, password, name)
                .onSuccess { _formState.value = AuthFormState.Idle }
                .onFailure { _formState.value = AuthFormState.Error(humanizeAuthError(it)) }
        }
    }

    fun signInWithGoogle(idToken: String) {
        _formState.value = AuthFormState.Submitting
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken)
                .onSuccess { _formState.value = AuthFormState.Idle }
                .onFailure { _formState.value = AuthFormState.Error(humanizeAuthError(it)) }
        }
    }

    fun signOut() {
        viewModelScope.launch { authRepository.signOut() }
    }

    fun clearError() {
        if (_formState.value is AuthFormState.Error) _formState.value = AuthFormState.Idle
    }

    private fun validate(email: String, password: String): Boolean {
        if (email.isBlank() || !email.contains("@")) {
            _formState.value = AuthFormState.Error("Enter a valid email")
            return false
        }
        if (password.length < 6) {
            _formState.value = AuthFormState.Error("Password must be at least 6 characters")
            return false
        }
        return true
    }

    /** Turn Firebase exceptions into something a human would actually want to read. */
    private fun humanizeAuthError(t: Throwable): String {
        val msg = t.message.orEmpty().lowercase()
        return when {
            "password is invalid" in msg || "invalid-credential" in msg ||
                "wrong-password" in msg -> "Incorrect email or password"
            "no user record" in msg || "user-not-found" in msg -> "No account found for that email"
            "email address is already" in msg || "email-already-in-use" in msg ->
                "An account with that email already exists"
            "network" in msg -> "Network error — check your connection"
            else -> t.message ?: "Something went wrong"
        }
    }
}

/** UI state for the sign-in / sign-up form (separate from auth-session state). */
sealed interface AuthFormState {
    data object Idle : AuthFormState
    data object Submitting : AuthFormState
    data class Error(val message: String) : AuthFormState
}
