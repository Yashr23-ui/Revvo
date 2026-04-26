package com.revvo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revvo.data.model.User
import com.revvo.data.repository.UserRepository
import com.revvo.ui.state.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the current user's profile. Mirrors the same architecture rules as
 * [RideViewModel].
 */
class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    /** Wrapped in [UiState] so screens can render Loading / NotSignedIn / Success uniformly.
     *  In Phase 3 the Empty state will mean "signed out" and trigger a redirect to login. */
    val userState: StateFlow<UiState<User>> = repository.observeCurrentUser()
        .map<User?, UiState<User>> { user ->
            if (user == null) UiState.Empty else UiState.Success(user)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = UiState.Loading
        )

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }
}
