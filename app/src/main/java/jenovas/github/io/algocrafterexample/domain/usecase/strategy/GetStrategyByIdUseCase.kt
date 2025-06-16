package jenovas.github.io.algocrafterexample.domain.usecase.strategy

import jenovas.github.io.algocrafterexample.domain.model.Strategy
import jenovas.github.io.algocrafterexample.domain.repository.StrategyRepository

/**
 * Use case for retrieving a single strategy by ID.
 * Encapsulates business logic for getting strategy details.
 */
class GetStrategyByIdUseCase(
    private val strategyRepository: StrategyRepository
) {

    /**
     * Gets a strategy by its ID.
     * @param id The strategy ID
     * @return The strategy if found, null otherwise
     * @throws IllegalArgumentException if ID is blank
     */
    suspend operator fun invoke(id: String): Strategy? {
        require(id.isNotBlank()) { "Strategy ID cannot be blank" }
        
        return strategyRepository.getStrategyById(id)
    }

    /**
     * Gets a strategy by ID and ensures it exists.
     * @param id The strategy ID
     * @return The strategy
     * @throws IllegalArgumentException if ID is blank or strategy not found
     */
    suspend fun getStrategyByIdOrThrow(id: String): Strategy {
        require(id.isNotBlank()) { "Strategy ID cannot be blank" }
        
        val strategy = strategyRepository.getStrategyById(id)
        requireNotNull(strategy) { "Strategy with ID '$id' not found" }
        
        return strategy
    }

    /**
     * Checks if a strategy exists by ID.
     * @param id The strategy ID
     * @return True if strategy exists, false otherwise
     */
    suspend fun strategyExists(id: String): Boolean {
        if (id.isBlank()) return false
        
        return strategyRepository.getStrategyById(id) != null
    }

    /**
     * Gets a strategy by ID only if it's active.
     * @param id The strategy ID
     * @return The strategy if found and active, null otherwise
     */
    suspend fun getActiveStrategyById(id: String): Strategy? {
        val strategy = invoke(id)
        return if (strategy?.isActive == true) strategy else null
    }
} 