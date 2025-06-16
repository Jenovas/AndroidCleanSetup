package jenovas.github.io.algocrafterexample.ui.screens.strategies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jenovas.github.io.algocrafterexample.domain.model.Strategy
import jenovas.github.io.algocrafterexample.domain.usecase.strategy.DeleteStrategyUseCase
import jenovas.github.io.algocrafterexample.domain.usecase.strategy.GetStrategiesUseCase
import jenovas.github.io.algocrafterexample.domain.usecase.strategy.GetStrategyByIdUseCase
import jenovas.github.io.algocrafterexample.domain.usecase.strategy.SaveStrategyUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StrategiesViewModel(
    private val getStrategiesUseCase: GetStrategiesUseCase,
    private val getStrategyByIdUseCase: GetStrategyByIdUseCase,
    private val deleteStrategyUseCase: DeleteStrategyUseCase,
    private val saveStrategyUseCase: SaveStrategyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StrategiesUiState())

    val state: StateFlow<StrategiesUiState> = combine(
        getStrategiesUseCase(),
        _uiState
    ) { strategies, currentState ->
        currentState.copy(
            isLoading = false,
            strategies = strategies.map { it.toStrategyItem() },
            error = null
        )
    }
        .catch { throwable ->
            emit(
                _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load strategies: ${throwable.message}"
                )
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            StrategiesUiState(isLoading = true)
        )

    private val _effects = Channel<StrategiesEffect>(capacity = Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun handleEvent(event: StrategiesEvent) {
        when (event) {
            is StrategiesEvent.LoadStrategies -> loadStrategies()
            is StrategiesEvent.RefreshStrategies -> refreshStrategies()
            is StrategiesEvent.NavigateToStrategyDetail -> navigateToStrategyDetail(event.strategyId)
            is StrategiesEvent.NavigateToCreateStrategy -> navigateToCreateStrategy()
            is StrategiesEvent.ShowDeleteConfirmation -> showDeleteConfirmation(event.strategyId, event.strategyName)
            is StrategiesEvent.ConfirmDelete -> confirmDelete()
            is StrategiesEvent.DismissDeleteConfirmation -> dismissDeleteConfirmation()
            is StrategiesEvent.ToggleStrategyActive -> toggleStrategyActive(event.strategyId)
            is StrategiesEvent.SearchStrategies -> searchStrategies(event.query)
            is StrategiesEvent.FilterByType -> filterByType(event.type)
            is StrategiesEvent.ShowActiveOnly -> showActiveOnly(event.activeOnly)
            is StrategiesEvent.DismissError -> dismissError()
            is StrategiesEvent.ShowMessage -> showMessage(event.message)
        }
    }

    private fun loadStrategies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            // The combine flow will automatically load strategies
        }
    }

    private fun refreshStrategies() {
        viewModelScope.launch {
            _effects.send(StrategiesEffect.ShowMessage("Strategies refreshed"))
        }
    }

    private fun navigateToStrategyDetail(strategyId: String) {
        viewModelScope.launch {
            //_effects.send(StrategiesEffect.NavigateToDetail(strategyId))
            _effects.send(StrategiesEffect.ShowMessage("Navigate to strategy: $strategyId"))
        }
    }

    private fun navigateToCreateStrategy() {
        viewModelScope.launch {
            //_effects.send(StrategiesEffect.NavigateToCreate)
            _effects.send(StrategiesEffect.ShowMessage("Navigate to create strategy"))
        }
    }

    private fun showDeleteConfirmation(strategyId: String, strategyName: String) {
        _uiState.value = _uiState.value.copy(
            deleteConfirmation = DeleteConfirmationState(
                strategyId = strategyId,
                strategyName = strategyName,
                isVisible = true
            )
        )
    }

    private fun confirmDelete() {
        val confirmation = _uiState.value.deleteConfirmation
        if (confirmation != null && confirmation.isVisible) {
            viewModelScope.launch {
                try {
                    val deleted = deleteStrategyUseCase(confirmation.strategyId)
                    if (deleted) {
                        _effects.send(StrategiesEffect.ShowMessage("Strategy '${confirmation.strategyName}' deleted successfully"))
                    } else {
                        _effects.send(StrategiesEffect.ShowMessage("Strategy not found"))
                    }
                } catch (e: Exception) {
                    _effects.send(StrategiesEffect.ShowMessage("Failed to delete strategy: ${e.message}"))
                } finally {
                    dismissDeleteConfirmation()
                }
            }
        }
    }

    private fun dismissDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(deleteConfirmation = null)
    }

    private fun toggleStrategyActive(strategyId: String) {
        viewModelScope.launch {
            try {
                val strategy = getStrategyByIdUseCase(strategyId)
                if (strategy != null) {

                    val updatedStrategy = if (strategy.isActive) {
                        strategy.deactivate()
                    } else {
                        strategy.activate()
                    }

                    saveStrategyUseCase(updatedStrategy)

                    val action = if (updatedStrategy.isActive) "activated" else "deactivated"
                    _effects.send(StrategiesEffect.ShowMessage("Strategy $action successfully"))
                } else {
                    _effects.send(StrategiesEffect.ShowMessage("Strategy not found"))
                }
            } catch (e: Exception) {
                _effects.send(StrategiesEffect.ShowMessage("Failed to toggle strategy: ${e.message}"))
            }
        }
    }

    private fun searchStrategies(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    private fun filterByType(type: String?) {
        _uiState.value = _uiState.value.copy(filterType = type)
    }

    private fun showActiveOnly(activeOnly: Boolean) {
        _uiState.value = _uiState.value.copy(showActiveOnly = activeOnly)
    }

    private fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _effects.send(StrategiesEffect.ShowMessage(message))
        }
    }

    private fun Strategy.toStrategyItem(): StrategyItem {
        return StrategyItem(
            id = id,
            name = name,
            description = description,
            isActive = isActive,
            strategyType = strategyType.name,
            lastUpdated = updatedAt.toString(),
            isRecentlyModified = isRecentlyModified(),
            tags = tags
        )
    }
}

data class StrategiesUiState(
    val isLoading: Boolean = false,
    val strategies: List<StrategyItem> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val filterType: String? = null,
    val showActiveOnly: Boolean = false,
    val deleteConfirmation: DeleteConfirmationState? = null
) {
    val hasStrategies: Boolean get() = strategies.isNotEmpty()
    val activeStrategiesCount: Int get() = strategies.count { it.isActive }
    val filteredStrategies: List<StrategyItem>
        get() {
            return strategies.filter { strategy ->
                val matchesSearch = if (searchQuery.isBlank()) {
                    true
                } else {
                    strategy.name.contains(searchQuery, ignoreCase = true) ||
                            strategy.description.contains(searchQuery, ignoreCase = true)
                }

                val matchesType = filterType?.let { type ->
                    strategy.strategyType.equals(type, ignoreCase = true)
                } != false

                val matchesActive = if (showActiveOnly) strategy.isActive else true

                matchesSearch && matchesType && matchesActive
            }
        }
}

data class StrategyItem(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val strategyType: String,
    val lastUpdated: String,
    val isRecentlyModified: Boolean,
    val tags: List<String> = emptyList()
)

data class DeleteConfirmationState(
    val strategyId: String,
    val strategyName: String,
    val isVisible: Boolean
)

sealed interface StrategiesEffect {
    data class ShowMessage(val message: String) : StrategiesEffect
    data class NavigateToDetail(val strategyId: String) : StrategiesEffect
    object NavigateToCreate : StrategiesEffect
}

sealed interface StrategiesEvent {
    object LoadStrategies : StrategiesEvent
    object RefreshStrategies : StrategiesEvent
    data class NavigateToStrategyDetail(val strategyId: String) : StrategiesEvent
    object NavigateToCreateStrategy : StrategiesEvent
    data class ShowDeleteConfirmation(val strategyId: String, val strategyName: String) : StrategiesEvent
    object ConfirmDelete : StrategiesEvent
    object DismissDeleteConfirmation : StrategiesEvent
    data class ToggleStrategyActive(val strategyId: String) : StrategiesEvent
    data class SearchStrategies(val query: String) : StrategiesEvent
    data class FilterByType(val type: String?) : StrategiesEvent
    data class ShowActiveOnly(val activeOnly: Boolean) : StrategiesEvent
    object DismissError : StrategiesEvent
    data class ShowMessage(val message: String) : StrategiesEvent
} 