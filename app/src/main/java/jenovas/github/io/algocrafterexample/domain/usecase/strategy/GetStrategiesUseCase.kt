package jenovas.github.io.algocrafterexample.domain.usecase.strategy

import jenovas.github.io.algocrafterexample.domain.model.Strategy
import jenovas.github.io.algocrafterexample.domain.repository.StrategyRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving strategies.
 * Encapsulates business logic for getting strategies with optional filtering.
 */
class GetStrategiesUseCase(
    private val strategyRepository: StrategyRepository
) {

    /**
     * Gets all strategies.
     * @return Flow of list of strategies
     */
    operator fun invoke(): Flow<List<Strategy>> {
        return strategyRepository.getStrategies()
    }

    /**
     * Gets only active strategies.
     * @return Flow of list of active strategies
     */
    fun getActiveStrategies(): Flow<List<Strategy>> {
        return strategyRepository.getActiveStrategies()
    }

    /**
     * Gets strategies sorted by most recently modified first.
     * @return Flow of list of strategies sorted by update time
     */
    fun getStrategiesSortedByRecentlyModified(): Flow<List<Strategy>> {
        return strategyRepository.getStrategies()
    }

    /**
     * Gets strategies ready for backtesting.
     * This includes only active strategies with parameters configured.
     * @return Flow of list of strategies ready for backtesting
     */
    fun getStrategiesReadyForBacktest(): Flow<List<Strategy>> {
        return strategyRepository.getActiveStrategies()
    }
} 