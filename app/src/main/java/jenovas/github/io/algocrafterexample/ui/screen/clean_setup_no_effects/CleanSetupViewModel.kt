package jenovas.github.io.algocrafterexample.ui.screen.clean_setup_no_effects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CleanSetupViewModel : ViewModel() {
    private val _state = MutableStateFlow<CleanSetupUiState>(CleanSetupUiState())
    val state: StateFlow<CleanSetupUiState> = _state
        .onStart { refreshData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CleanSetupUiState()
        )

    fun handleEvent(event: CleanSetupScreenEvent) {
        when (event) {
            is CleanSetupScreenEvent.Refresh -> {
                refreshData()
            }

            is CleanSetupScreenEvent.ToggleFavourite -> {
                handeFavouriteEvent(event.clickedDataClass)
            }

            is CleanSetupScreenEvent.SnackbarShown -> {
                handleSnackbarShownEvent()
            }

            is CleanSetupScreenEvent.NavigateBack -> {
                handleNavigateBackEvent()
            }

            is CleanSetupScreenEvent.NavigateToHome -> {
                handleNavigateToHomeEvent()
            }

            is CleanSetupScreenEvent.NavigationHandled -> {
                handleNavigateHandledEvent()
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _state.update { current ->
                current.copy(
                    contentState = current.contentState.copy(
                        isLoading = true,
                        snackbarMessage = "",
                    )
                )
            }

            try {
                val data = mutableListOf<CleanSetupDataClass>()
                for (i in 0..99) {
                    data.add(CleanSetupDataClass("Item $i", false))
                }
                _state.update { current ->
                    current.copy(
                        contentState = current.contentState.copy(
                            isLoading = false,
                            snackbarMessage = "",
                            cleanSetupDataClassList = data
                        )
                    )
                }
            } catch (e: Exception) {
                _state.update { current ->
                    current.copy(
                        contentState = current.contentState.copy(
                            isLoading = false,
                            snackbarMessage = e.message.orEmpty(),
                        )
                    )
                }
            }
        }
    }

    private fun handeFavouriteEvent(
        clickedDataClass: CleanSetupDataClass
    ) {
        _state.update { current ->
            current.contentState.cleanSetupDataClassList.map {
                if (it.name == clickedDataClass.name) {
                    it.copy(isFavourite = !it.isFavourite)
                } else it
            }.let { updatedList ->
                current.copy(
                    contentState = current.contentState.copy(
                        snackbarMessage = "Item ${clickedDataClass.name} is ${if (clickedDataClass.isFavourite) "removed from" else "added to"} favourites",
                        cleanSetupDataClassList = updatedList
                    )
                )
            }
        }
    }

    private fun handleSnackbarShownEvent() {
        _state.update { current ->
            current.copy(
                contentState = current.contentState.copy(
                    snackbarMessage = ""
                )
            )
        }
    }

    private fun handleNavigateBackEvent() {
        _state.update { current ->
            current.copy(
                navTarget = CleanSetupNavTarget.Back
            )
        }
    }

    private fun handleNavigateToHomeEvent() {
        _state.update { current ->
            current.copy(
                navTarget = CleanSetupNavTarget.Home
            )
        }
    }

    private fun handleNavigateHandledEvent() {
        _state.update { current ->
            current.copy(
                navTarget = CleanSetupNavTarget.None
            )
        }
    }
}

data class CleanSetupUiState(
    val contentState: CleanSetupScreenContentState = CleanSetupScreenContentState(),
    val navTarget: CleanSetupNavTarget = CleanSetupNavTarget.None
)

data class CleanSetupScreenContentState(
    val isLoading: Boolean = false,
    val snackbarMessage: String = "",
    val cleanSetupDataClassList: List<CleanSetupDataClass> = emptyList()
)

data class CleanSetupDataClass(
    val name: String,
    val isFavourite: Boolean
) {
    companion object {
        fun fakeList(): List<CleanSetupDataClass> {
            return listOf(
                CleanSetupDataClass("Item 0", false),
                CleanSetupDataClass("Item 1", true),
                CleanSetupDataClass("Item 2", false),
                CleanSetupDataClass("Item 3", false),
                CleanSetupDataClass("Item 4", false),
                CleanSetupDataClass("Item 5", true),
                CleanSetupDataClass("Item 6", true),
                CleanSetupDataClass("Item 7", false),
                CleanSetupDataClass("Item 8", false),
                CleanSetupDataClass("Item 9", false),
            )
        }
    }
}

sealed interface CleanSetupNavTarget {
    object None : CleanSetupNavTarget
    object Back : CleanSetupNavTarget
    object Home : CleanSetupNavTarget
}

sealed interface CleanSetupScreenEvent {
    object Refresh : CleanSetupScreenEvent
    data class ToggleFavourite(val clickedDataClass: CleanSetupDataClass) : CleanSetupScreenEvent
    object SnackbarShown : CleanSetupScreenEvent
    object NavigateBack : CleanSetupScreenEvent
    object NavigateToHome : CleanSetupScreenEvent
    object NavigationHandled : CleanSetupScreenEvent
}