package jenovas.github.io.algocrafterexample.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jenovas.github.io.algocrafterexample.ui.navigation.Legal
import jenovas.github.io.algocrafterexample.ui.navigation.Strategies
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {

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
            is MenuScreenEvent.NavigateToStrategies -> navigateToStrategies()
            is MenuScreenEvent.NavigateToBacktestResults -> navigateToBacktestResults()
            is MenuScreenEvent.NavigateToMarketData -> navigateToMarketData()
            is MenuScreenEvent.NavigateToSettings -> navigateToSettings()
            is MenuScreenEvent.NavigateToLegal -> navigateToLegal()
            is MenuScreenEvent.ShowMessage -> showMessage(event.message)
        }
    }

    private fun navigateToAIBuilder() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(AIStrategyBuilder))
            _effects.send(MenuEffect.ShowMessage("AI Strategy Builder coming soon"))
        }
    }

    private fun navigateToCreateStrategy() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(CreateStrategy))
            _effects.send(MenuEffect.ShowMessage("Create Strategy coming soon"))
        }
    }

    private fun navigateToStrategies() {
        viewModelScope.launch {
            _effects.send(MenuEffect.Navigate(Strategies))
        }
    }

    private fun navigateToBacktestResults() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(BacktestResults))
            _effects.send(MenuEffect.ShowMessage("Backtest Results coming soon"))
        }
    }

    private fun navigateToMarketData() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(MarketData))
            _effects.send(MenuEffect.ShowMessage("Market Data coming soon"))
        }
    }

    private fun navigateToSettings() {
        viewModelScope.launch {
            //_effects.send(MenuEffect.Navigate(Settings))
            _effects.send(MenuEffect.ShowMessage("Settings coming soon"))
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
    val isLoading: Boolean = false
)

sealed interface MenuEffect {
    data class ShowMessage(val message: String) : MenuEffect
    data class Navigate<T : Any>(val destination: T) : MenuEffect
}

sealed interface MenuScreenEvent {
    object NavigateToAIBuilder : MenuScreenEvent
    object NavigateToCreateStrategy : MenuScreenEvent
    object NavigateToStrategies : MenuScreenEvent
    object NavigateToBacktestResults : MenuScreenEvent
    object NavigateToMarketData : MenuScreenEvent
    object NavigateToSettings : MenuScreenEvent
    object NavigateToLegal : MenuScreenEvent
    data class ShowMessage(val message: String) : MenuScreenEvent
} 