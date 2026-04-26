package com.revvo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revvo.data.model.Ride
import com.revvo.data.model.RideStatus
import com.revvo.data.repository.RideRepository
import com.revvo.ui.model.RideCardData
import com.revvo.ui.model.toRideCardData
import com.revvo.ui.state.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel for everything ride-related.
 *
 * Architecture rules followed here:
 *  1. Repository is INJECTED, not constructed. ([ViewModelFactory] does the wiring.)
 *  2. UI state is DERIVED from a single source (the repository's flow), never duplicated.
 *  3. All writes happen inside `viewModelScope.launch { … }` so they're cancelled on
 *     ViewModel cleared.
 *  4. Side effects (one-shot events like "ride created — navigate away") go through
 *     [_events] as a SharedFlow so they don't replay on config change.
 */
class RideViewModel(
    private val repository: RideRepository
) : ViewModel() {

    /** Stream of all rides as the data layer sees them. */
    val rides: StateFlow<List<Ride>> = repository.observeRides()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptyList()
        )

    /**
     * UI-shaped projection of [rides]. Derived — NOT a separate state to keep in sync.
     * If `rides` changes, this changes for free.
     */
    val rideCards: StateFlow<List<RideCardData>> = repository.observeRides()
        .map { list -> list.map { it.toRideCardData() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptyList()
        )

    // -- Selected ride (for RideDetailsScreen) ----------------------------------------------------

    private val _selectedRideId = MutableStateFlow<String?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val selectedRide: StateFlow<UiState<Ride>> = _selectedRideId
        .flatMapLatest { id ->
            if (id == null) {
                kotlinx.coroutines.flow.flowOf<UiState<Ride>>(UiState.Empty)
            } else {
                repository.observeRideById(id).map<Ride?, UiState<Ride>> { ride ->
                    if (ride == null) UiState.Error("Ride not found")
                    else UiState.Success(ride)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = UiState.Loading
        )

    fun selectRide(id: String) {
        _selectedRideId.value = id
    }

    fun clearSelection() {
        _selectedRideId.value = null
    }

    // -- One-shot events (navigation triggers, snackbars) -----------------------------------------

    private val _events = MutableSharedFlow<RideEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    // -- Actions ----------------------------------------------------------------------------------

    /**
     * Validates the form and creates the ride. Emits [RideEvent.RideCreated] on success
     * with the new ride id so the UI can navigate to it.
     */
    fun createRide(form: CreateRideForm) {
        val validation = form.validate()
        if (validation is FormValidation.Invalid) {
            viewModelScope.launch { _events.emit(RideEvent.ValidationFailed(validation.errors)) }
            return
        }

        viewModelScope.launch {
            try {
                val ride = Ride(
                    id = UUID.randomUUID().toString(),
                    title = form.title.trim(),
                    startLocation = form.startLocation.trim(),
                    endLocation = form.endLocation.trim(),
                    displayDate = form.dateTime.trim(),
                    dateMillis = System.currentTimeMillis(), // TODO: parse form.dateTime properly
                    distanceKm = 0,
                    maxRiders = form.maxRiders.toInt(),
                    joinedRiders = 0,
                    description = form.description.trim(),
                    hostId = "user-1", // TODO: from AuthRepository in Phase 3
                    hostName = "You",
                    status = RideStatus.UPCOMING
                )
                val saved = repository.addRide(ride)
                _events.emit(RideEvent.RideCreated(saved.id))
            } catch (t: Throwable) {
                _events.emit(RideEvent.Error("Failed to create ride: ${t.message}"))
            }
        }
    }

    fun joinRide(rideId: String) {
        viewModelScope.launch {
            try {
                repository.joinRide(rideId)
                _events.emit(RideEvent.RideJoined(rideId))
            } catch (t: Throwable) {
                _events.emit(RideEvent.Error("Failed to join ride: ${t.message}"))
            }
        }
    }

    fun leaveRide(rideId: String) {
        viewModelScope.launch {
            repository.leaveRide(rideId)
        }
    }

    private companion object {
        /** Keeps the upstream flow alive for 5s after the last subscriber disappears
         *  (covers config changes & quick screen switches without re-fetching). */
        const val STOP_TIMEOUT_MS = 5_000L
    }
}

/** One-shot events emitted by the ViewModel. Collected by the UI via `LaunchedEffect`. */
sealed interface RideEvent {
    data class RideCreated(val rideId: String) : RideEvent
    data class RideJoined(val rideId: String) : RideEvent
    data class ValidationFailed(val errors: Map<String, String>) : RideEvent
    data class Error(val message: String) : RideEvent
}

/** Form input state. Lives in the screen as `remember { mutableStateOf(...) }` and is
 *  passed to the VM only at submit time. Validation lives here so the rule is testable. */
data class CreateRideForm(
    val title: String,
    val startLocation: String,
    val endLocation: String,
    val dateTime: String,
    val maxRiders: String,
    val description: String
) {
    fun validate(): FormValidation {
        val errors = mutableMapOf<String, String>()
        if (title.isBlank()) errors["title"] = "Title is required"
        if (startLocation.isBlank()) errors["startLocation"] = "Start location is required"
        if (endLocation.isBlank()) errors["endLocation"] = "Destination is required"
        if (dateTime.isBlank()) errors["dateTime"] = "Date & time is required"
        val parsedMax = maxRiders.toIntOrNull()
        if (parsedMax == null || parsedMax <= 0) errors["maxRiders"] = "Must be a positive number"
        return if (errors.isEmpty()) FormValidation.Valid else FormValidation.Invalid(errors)
    }
}

sealed interface FormValidation {
    data object Valid : FormValidation
    data class Invalid(val errors: Map<String, String>) : FormValidation
}
