package jenovas.github.io.algocrafterexample.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jenovas.github.io.algocrafterexample.ui.navigation.Legal
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MenuViewModel() : ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            MenuUiState()
        )

    private val _effects = Channel<MenuEffect>(capacity = Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun handleEvent(event: MenuScreenEvent) {
        when (event) {
            is MenuScreenEvent.NavigateToAIBuilder -> navigateToAIBuilder()
            is MenuScreenEvent.NavigateToCreateStrategy -> navigateToCreateStrategy()
            is MenuScreenEvent.NavigateToSettings -> navigateToSettings()
            is MenuScreenEvent.NavigateToDetails -> navigateToDetails()
            is MenuScreenEvent.NavigateToLegal -> navigateToLegal()
            is MenuScreenEvent.ShowMessage -> showMessage(event.message)
        }
    }

    private fun navigateToAIBuilder() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(AIStrategyBuilder))
        }
    }

    private fun navigateToCreateStrategy() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(CreateStrategy))
        }
    }

    private fun navigateToSettings() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(Settings))
        }
    }

    private fun navigateToDetails() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(StrategyDetails))
        }
    }

    private fun navigateToLegal() {
        viewModelScope.launch {
            _effects.send(MenuEffect.Navigate(Legal))
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _effects.send(MenuEffect.ShowMessage(message))
        }
    }
}

data class MenuUiState(
    val isLoading: Boolean = false,
    val strategies: List<StrategyItem> = emptyList(),
)

data class StrategyItem(
    val id: String,
    val name: String,
    val description: String
)

sealed interface MenuEffect {
    data class ShowMessage(val message: String) : MenuEffect
    data class Navigate<T : Any>(val destination: T) : MenuEffect
}

sealed interface MenuScreenEvent {
    object NavigateToAIBuilder : MenuScreenEvent
    object NavigateToCreateStrategy : MenuScreenEvent
    object NavigateToSettings : MenuScreenEvent
    data class NavigateToDetails(val id: String) : MenuScreenEvent
    object NavigateToLegal : MenuScreenEvent
    data class ShowMessage(val message: String) : MenuScreenEvent
} 