package jenovas.github.io.algocrafterexample.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState())
    val state: StateFlow<HomeScreenUiState> = _state
        .onStart { }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeScreenUiState()
        )

    fun handleEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.NavigateBack -> {
                handleNavigateBackEvent()
            }

            is HomeScreenEvent.NavigateToCleanSetupNoEffects -> {
                handleNavigateToCleanSetupNoEffectsEvent()
            }

            is HomeScreenEvent.NavigateToCleanSetupWithEffects -> {
                handleNavigateToCleanSetupWithEffectsEvent()
            }

            is HomeScreenEvent.NavigationHandled -> {
                handleNavigateHandledEvent()
            }
        }
    }

    private fun handleNavigateBackEvent() {
        _state.update { current ->
            current.copy(
                navTarget = HomeScreenNavTarget.Back
            )
        }
    }

    private fun handleNavigateToCleanSetupNoEffectsEvent() {
        _state.update { current ->
            current.copy(
                navTarget = HomeScreenNavTarget.CleanSetupNoEffects
            )
        }
    }

    private fun handleNavigateToCleanSetupWithEffectsEvent() {
        _state.update { current ->
            current.copy(
                navTarget = HomeScreenNavTarget.CleanSetupWithEffects
            )
        }
    }

    private fun handleNavigateHandledEvent() {
        _state.update { current ->
            current.copy(
                navTarget = HomeScreenNavTarget.None
            )
        }
    }
}

data class HomeScreenUiState(
    val contentState: HomeScreenContentState = HomeScreenContentState(),
    val navTarget: HomeScreenNavTarget = HomeScreenNavTarget.None
)

data class HomeScreenContentState(
    val isLoading: Boolean = false,
    val snackbarMessage: String = "",
)

sealed interface HomeScreenNavTarget {
    object None : HomeScreenNavTarget
    object Back : HomeScreenNavTarget
    object CleanSetupNoEffects : HomeScreenNavTarget
    object CleanSetupWithEffects : HomeScreenNavTarget
}

sealed class HomeScreenEvent {
    object NavigateBack : HomeScreenEvent()
    object NavigateToCleanSetupNoEffects : HomeScreenEvent()
    object NavigateToCleanSetupWithEffects : HomeScreenEvent()
    object NavigationHandled : HomeScreenEvent()
}