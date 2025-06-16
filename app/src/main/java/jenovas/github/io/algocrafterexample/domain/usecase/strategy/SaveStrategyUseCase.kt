package jenovas.github.io.algocrafterexample.domain.usecase.strategy

import jenovas.github.io.algocrafterexample.domain.model.Strategy
import jenovas.github.io.algocrafterexample.domain.repository.StrategyRepository
import java.time.LocalDateTime
import java.util.UUID

/**
 * Use case for saving strategies.
 * Encapsulates business logic for creating and updating strategies with validation.
 */
class SaveStrategyUseCase(
    private val strategyRepository: StrategyRepository
) {

    /**
     * Saves a strategy (create or update).
     * @param strategy The strategy to save
     * @return The saved strategy with updated timestamps
     * @throws IllegalArgumentException if strategy validation fails
     */
    suspend operator fun invoke(strategy: Strategy): Strategy {
        validateStrategy(strategy)

        val strategyToSave = if (isNewStrategy(strategy)) {
            strategy.copy(
                id = if (strategy.id.isBlank()) generateStrategyId() else strategy.id,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        } else {
            strategy.withUpdatedTimestamp()
        }
        
        return strategyRepository.saveStrategy(strategyToSave)
    }

    /**
     * Creates a new strategy with generated ID and timestamps.
     * @param strategy The strategy template (ID will be generated)
     * @return The saved strategy with generated ID and timestamps
     */
    suspend fun createNewStrategy(strategy: Strategy): Strategy {
        val newStrategy = strategy.copy(
            id = generateStrategyId(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        return invoke(newStrategy)
    }

    /**
     * Updates an existing strategy.
     * @param strategy The strategy to update (must have existing ID)
     * @return The updated strategy
     * @throws IllegalArgumentException if strategy doesn't exist
     */
    suspend fun updateStrategy(strategy: Strategy): Strategy {
        require(strategy.id.isNotBlank()) { "Strategy ID is required for updates" }

        val existingStrategy = strategyRepository.getStrategyById(strategy.id)
        requireNotNull(existingStrategy) { "Strategy with ID ${strategy.id} not found" }
        
        return invoke(strategy)
    }

    private fun validateStrategy(strategy: Strategy) {
        require(strategy.name.trim().isNotEmpty()) { "Strategy name cannot be empty or whitespace" }
        require(strategy.description.trim().isNotEmpty()) { "Strategy description cannot be empty or whitespace" }

        if (strategy.parameters.isNotEmpty()) {
            validateStrategyParameters(strategy.parameters)
        }
    }

    private fun validateStrategyParameters(parameters: Map<String, Any>) {
        // Basic parameter validation
        parameters.forEach { (key, value) ->
            require(key.isNotBlank()) { "Parameter key cannot be blank" }
            require(value.toString().isNotBlank()) { "Parameter value for '$key' cannot be blank" }
        }
    }

    private fun isNewStrategy(strategy: Strategy): Boolean {
        return strategy.id.isBlank() || strategy.createdAt == strategy.updatedAt
    }

    private fun generateStrategyId(): String {
        return "strategy_${UUID.randomUUID()}"
    }
} 