package jenovas.github.io.algocrafterexample.domain.repository

import jenovas.github.io.algocrafterexample.domain.model.Strategy
import jenovas.github.io.algocrafterexample.domain.model.StrategyType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

/**
 * Mock implementation of StrategyRepository for demonstration purposes.
 * This will be replaced with a real implementation when the data layer is created.
 */
class MockStrategyRepository : StrategyRepository {

    private val strategies = MutableStateFlow(createSampleStrategies())

    override fun getStrategies(): Flow<List<Strategy>> {
        return strategies
    }

    override fun getActiveStrategies(): Flow<List<Strategy>> {
        return strategies.map { list -> list.filter { it.isActive } }
    }

    override fun getStrategiesByType(type: StrategyType): Flow<List<Strategy>> {
        return strategies.map { list -> list.filter { it.strategyType == type } }
    }

    override suspend fun getStrategyById(id: String): Strategy? {
        return strategies.value.find { it.id == id }
    }

    override suspend fun saveStrategy(strategy: Strategy): Strategy {
        val currentStrategies = strategies.value.toMutableList()
        val existingIndex = currentStrategies.indexOfFirst { it.id == strategy.id }
        
        val savedStrategy = if (existingIndex >= 0) {
            // Update existing
            currentStrategies[existingIndex] = strategy
            strategy
        } else {
            // Add new
            currentStrategies.add(strategy)
            strategy
        }
        
        strategies.value = currentStrategies
        return savedStrategy
    }

    override suspend fun deleteStrategy(id: String): Boolean {
        val currentStrategies = strategies.value.toMutableList()
        val removed = currentStrategies.removeIf { it.id == id }
        if (removed) {
            strategies.value = currentStrategies
        }
        return removed
    }

    override fun searchStrategies(query: String): Flow<List<Strategy>> {
        return strategies.map { list ->
            list.filter { strategy ->
                strategy.name.contains(query, ignoreCase = true) ||
                strategy.description.contains(query, ignoreCase = true)
            }
        }
    }

    override fun getStrategiesWithTags(tags: List<String>): Flow<List<Strategy>> {
        return strategies.map { list ->
            list.filter { strategy ->
                strategy.tags.any { tag -> tags.contains(tag) }
            }
        }
    }

    override suspend fun activateStrategy(id: String): Boolean {
        val strategy = getStrategyById(id)
        return if (strategy != null) {
            saveStrategy(strategy.activate())
            true
        } else {
            false
        }
    }

    override suspend fun deactivateStrategy(id: String): Boolean {
        val strategy = getStrategyById(id)
        return if (strategy != null) {
            saveStrategy(strategy.deactivate())
            true
        } else {
            false
        }
    }

    override suspend fun getStrategyCount(): Int {
        return strategies.value.size
    }

    override suspend fun getActiveStrategyCount(): Int {
        return strategies.value.count { it.isActive }
    }

    private fun createSampleStrategies(): List<Strategy> {
        val now = LocalDateTime.now()
        return listOf(
            Strategy(
                id = "strategy_1",
                name = "Moving Average Crossover",
                description = "Simple trend-following strategy using 20 and 50 period moving averages",
                strategyType = StrategyType.TREND_FOLLOWING,
                isActive = true,
                createdAt = now.minusDays(10),
                updatedAt = now.minusDays(2),
                parameters = mapOf(
                    "fastPeriod" to 20,
                    "slowPeriod" to 50,
                    "symbol" to "AAPL"
                ),
                tags = listOf("beginner", "trend", "moving-average")
            ),
            Strategy(
                id = "strategy_2",
                name = "RSI Mean Reversion",
                description = "Buy oversold and sell overbought conditions using RSI indicator",
                strategyType = StrategyType.MEAN_REVERSION,
                isActive = true,
                createdAt = now.minusDays(7),
                updatedAt = now.minusDays(1),
                parameters = mapOf(
                    "rsiPeriod" to 14,
                    "oversoldLevel" to 30,
                    "overboughtLevel" to 70,
                    "symbol" to "MSFT"
                ),
                tags = listOf("intermediate", "oscillator", "rsi")
            ),
            Strategy(
                id = "strategy_3",
                name = "Bollinger Band Squeeze",
                description = "Momentum strategy that trades breakouts from low volatility periods",
                strategyType = StrategyType.MOMENTUM,
                isActive = false,
                createdAt = now.minusDays(15),
                updatedAt = now.minusDays(5),
                parameters = mapOf(
                    "period" to 20,
                    "standardDeviations" to 2.0,
                    "symbol" to "GOOGL"
                ),
                tags = listOf("advanced", "volatility", "bollinger")
            ),
            Strategy(
                id = "strategy_4",
                name = "AI-Generated Momentum",
                description = "Machine learning based momentum strategy with adaptive parameters",
                strategyType = StrategyType.CUSTOM,
                isActive = true,
                createdAt = now.minusDays(3),
                updatedAt = now.minusHours(6),
                parameters = mapOf(
                    "lookbackPeriod" to 30,
                    "confidenceThreshold" to 0.75,
                    "symbol" to "TSLA"
                ),
                tags = listOf("ai", "machine-learning", "momentum")
            )
        )
    }
} 