package jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CleanSetupHomeScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow<CleanSetupHomeScreenUiState>(CleanSetupHomeScreenUiState())
    val state: StateFlow<CleanSetupHomeScreenUiState> = _state
        .onStart { }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CleanSetupHomeScreenUiState()
        )

    fun handleEvent(event: CleanSetupHomeScreenEvent) {
        when (event) {
            is CleanSetupHomeScreenEvent.NavigateBack -> {
                handleNavigateBackEvent()
            }

            is CleanSetupHomeScreenEvent.NavigateToCleanSetupNoEffects -> {
                handleNavigateToCleanSetupNoEffectsEvent()
            }

            is CleanSetupHomeScreenEvent.NavigateToCleanSetupWithEffects -> {
                handleNavigateToCleanSetupWithEffectsEvent()
            }

            is CleanSetupHomeScreenEvent.NavigationHandled -> {
                handleNavigateHandledEvent()
            }
        }
    }

    private fun handleNavigateBackEvent() {
        _state.update { current ->
            current.copy(
                navTarget = CleanSetupHomeScreenNavTarget.Back
            )
        }
    }

    private fun handleNavigateToCleanSetupNoEffectsEvent() {
        _state.update { current ->
            current.copy(
                navTarget = CleanSetupHomeScreenNavTarget.CleanSetupNoEffects
            )
        }
    }

    private fun handleNavigateToCleanSetupWithEffectsEvent() {
        _state.update { current ->
            current.copy(
                navTarget = CleanSetupHomeScreenNavTarget.CleanSetupWithEffects
            )
        }
    }

    private fun handleNavigateHandledEvent() {
        _state.update { current ->
            current.copy(
                navTarget = CleanSetupHomeScreenNavTarget.None
            )
        }
    }
}

data class CleanSetupHomeScreenUiState(
    val contentState: CleanSetupHomeScreenContentState = CleanSetupHomeScreenContentState(),
    val navTarget: CleanSetupHomeScreenNavTarget = CleanSetupHomeScreenNavTarget.None
)

data class CleanSetupHomeScreenContentState(
    val isLoading: Boolean = false,
    val snackbarMessage: String = "",
)

sealed interface CleanSetupHomeScreenNavTarget {
    object None : CleanSetupHomeScreenNavTarget
    object Back : CleanSetupHomeScreenNavTarget
    object CleanSetupNoEffects : CleanSetupHomeScreenNavTarget
    object CleanSetupWithEffects : CleanSetupHomeScreenNavTarget
}

sealed class CleanSetupHomeScreenEvent {
    object NavigateBack : CleanSetupHomeScreenEvent()
    object NavigateToCleanSetupNoEffects : CleanSetupHomeScreenEvent()
    object NavigateToCleanSetupWithEffects : CleanSetupHomeScreenEvent()
    object NavigationHandled : CleanSetupHomeScreenEvent()
}