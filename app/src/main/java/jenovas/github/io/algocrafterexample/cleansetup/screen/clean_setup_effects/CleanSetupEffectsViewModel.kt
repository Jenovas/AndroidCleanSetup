package jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_effects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CleanSetupEffectsViewModel : ViewModel() {
    private val _state = MutableStateFlow<CleanSetupEffectsUiState>(CleanSetupEffectsUiState())
    val state: StateFlow<CleanSetupEffectsUiState> = _state
        .onStart { refreshData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CleanSetupEffectsUiState()
        )

    private val _effects = Channel<CleanSetupEffect>(capacity = Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun handleEvent(event: CleanSetupEffectsScreenEvent) {
        when (event) {
            is CleanSetupEffectsScreenEvent.Refresh -> {
                refreshData()
            }

            is CleanSetupEffectsScreenEvent.ToggleFavourite -> {
                handeFavouriteEvent(event.clickedDataClass)
            }

            is CleanSetupEffectsScreenEvent.NavigateBack -> {
                sendNavigateBackEvent()
            }

            is CleanSetupEffectsScreenEvent.NavigateToHome -> {
                sendNavigateToHomeEvent()
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _state.update { current ->
                current.copy(
                    isLoading = true,
                )
            }

            delay(2000)

            try {
                val data = mutableListOf<CleanSetupEffectsDataClass>()
                for (i in 0..99) {
                    data.add(CleanSetupEffectsDataClass("Item $i", false))
                }
                _state.update { current ->
                    current.copy(
                        isLoading = false,
                        cleanSetupDataClassList = data
                    )
                }
            } catch (e: Exception) {
                _state.update { current ->
                    current.copy(
                        isLoading = false,
                    )
                }

                val snackbarMessage = "Error loading data: ${e.message}"
                sendSnackbarMessage(snackbarMessage)
            }
        }
    }

    private fun handeFavouriteEvent(
        clickedDataClass: CleanSetupEffectsDataClass
    ) {
        _state.update { current ->
            current.cleanSetupDataClassList.map {
                if (it.name == clickedDataClass.name) {
                    it.copy(isFavourite = !it.isFavourite)
                } else it
            }.let { updatedList ->
                current.copy(
                    cleanSetupDataClassList = updatedList
                )
            }
        }

        val snackbarMessage = "Item ${clickedDataClass.name} is ${if (clickedDataClass.isFavourite) "removed from" else "added to"} favourites"
        sendSnackbarMessage(snackbarMessage)
    }

    private fun sendSnackbarMessage(message: String) {
        viewModelScope.launch { _effects.send(CleanSetupEffect.ShowMessage(message)) }
    }

    private fun sendNavigateBackEvent() {
        viewModelScope.launch { _effects.send(CleanSetupEffect.NavigateBack) }
    }

    private fun sendNavigateToHomeEvent() {
        viewModelScope.launch { _effects.send(CleanSetupEffect.NavigateToHome) }
    }
}

data class CleanSetupEffectsUiState(
    val isLoading: Boolean = false,
    val cleanSetupDataClassList: List<CleanSetupEffectsDataClass> = emptyList()
)

data class CleanSetupEffectsDataClass(
    val name: String,
    val isFavourite: Boolean
) {
    companion object {
        fun fakeList(): List<CleanSetupEffectsDataClass> {
            return listOf(
                CleanSetupEffectsDataClass("Item 0", false),
                CleanSetupEffectsDataClass("Item 1", true),
                CleanSetupEffectsDataClass("Item 2", false),
                CleanSetupEffectsDataClass("Item 3", false),
                CleanSetupEffectsDataClass("Item 4", false),
                CleanSetupEffectsDataClass("Item 5", true),
                CleanSetupEffectsDataClass("Item 6", true),
                CleanSetupEffectsDataClass("Item 7", false),
                CleanSetupEffectsDataClass("Item 8", false),
                CleanSetupEffectsDataClass("Item 9", false),
            )
        }
    }
}

sealed interface CleanSetupEffect {
    data class ShowMessage(val message: String) : CleanSetupEffect
    object NavigateToHome : CleanSetupEffect
    object NavigateBack : CleanSetupEffect
}

sealed interface CleanSetupEffectsScreenEvent {
    object Refresh : CleanSetupEffectsScreenEvent
    data class ToggleFavourite(val clickedDataClass: CleanSetupEffectsDataClass) :
        CleanSetupEffectsScreenEvent

    object NavigateBack : CleanSetupEffectsScreenEvent
    object NavigateToHome : CleanSetupEffectsScreenEvent
}