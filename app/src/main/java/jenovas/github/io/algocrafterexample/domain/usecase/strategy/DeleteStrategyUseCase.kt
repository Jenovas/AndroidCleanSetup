package jenovas.github.io.algocrafterexample.domain.usecase.strategy

import jenovas.github.io.algocrafterexample.domain.repository.StrategyRepository

/**
 * Use case for deleting strategies.
 * Encapsulates business logic for strategy deletion with validation.
 */
class DeleteStrategyUseCase(
    private val strategyRepository: StrategyRepository
) {

    /**
     * Deletes a strategy by ID.
     * @param id The strategy ID to delete
     * @return True if deleted successfully, false if strategy not found
     * @throws IllegalArgumentException if ID is blank
     */
    suspend operator fun invoke(id: String): Boolean {
        require(id.isNotBlank()) { "Strategy ID cannot be blank" }

        val existingStrategy = strategyRepository.getStrategyById(id)
        if (existingStrategy == null) {
            return false
        }

        validateDeletion(existingStrategy.id)
        
        return strategyRepository.deleteStrategy(id)
    }

    /**
     * Soft deletes a strategy by deactivating it instead of removing it.
     * @param id The strategy ID to deactivate
     * @return True if deactivated successfully, false if strategy not found
     * @throws IllegalArgumentException if ID is blank
     */
    suspend fun deactivateStrategy(id: String): Boolean {
        require(id.isNotBlank()) { "Strategy ID cannot be blank" }
        
        return strategyRepository.deactivateStrategy(id)
    }

    /**
     * Checks if a strategy can be safely deleted.
     * @param id The strategy ID to check
     * @return True if strategy can be deleted, false otherwise
     */
    suspend fun canDeleteStrategy(id: String): Boolean {
        require(id.isNotBlank()) { "Strategy ID cannot be blank" }
        
        val strategy = strategyRepository.getStrategyById(id)
        if (strategy == null) {
            return false
        }
        
        // Add business rules for when a strategy cannot be deleted
        // For example: if it's currently being used in active backtests
        // For now, we allow deletion of any existing strategy
        return true
    }

    private fun validateDeletion(id: String) {
        // Add any business rules that prevent deletion
        // For example:
        // - Strategy is currently running in a backtest
        // - Strategy has active trades
        // - Strategy is marked as protected/system strategy
        
        // For now, we allow all deletions
        // This is where you would add business-specific validation
    }
} 