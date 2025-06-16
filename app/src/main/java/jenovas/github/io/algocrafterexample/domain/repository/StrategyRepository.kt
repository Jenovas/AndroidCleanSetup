package jenovas.github.io.algocrafterexample.domain.repository

import jenovas.github.io.algocrafterexample.domain.model.Strategy
import jenovas.github.io.algocrafterexample.domain.model.StrategyType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for strategy data operations.
 * This interface defines the contract for strategy persistence and retrieval,
 * following Clean Architecture principles by being part of the domain layer.
 */
interface StrategyRepository {

    /**
     * Gets all strategies as a Flow for reactive updates.
     * @return Flow of list of strategies
     */
    fun getStrategies(): Flow<List<Strategy>>

    /**
     * Gets all active strategies as a Flow.
     * @return Flow of list of active strategies
     */
    fun getActiveStrategies(): Flow<List<Strategy>>

    /**
     * Gets strategies by type as a Flow.
     * @param type The strategy type to filter by
     * @return Flow of list of strategies matching the type
     */
    fun getStrategiesByType(type: StrategyType): Flow<List<Strategy>>

    /**
     * Gets a strategy by its ID.
     * @param id The strategy ID
     * @return The strategy if found, null otherwise
     */
    suspend fun getStrategyById(id: String): Strategy?

    /**
     * Saves a strategy (create or update).
     * @param strategy The strategy to save
     * @return The saved strategy with updated timestamps
     */
    suspend fun saveStrategy(strategy: Strategy): Strategy

    /**
     * Deletes a strategy by ID.
     * @param id The strategy ID to delete
     * @return True if deleted successfully, false otherwise
     */
    suspend fun deleteStrategy(id: String): Boolean

    /**
     * Searches strategies by name or description.
     * @param query The search query
     * @return Flow of list of strategies matching the search query
     */
    fun searchStrategies(query: String): Flow<List<Strategy>>

    /**
     * Gets strategies with specific tags.
     * @param tags The list of tags to filter by
     * @return Flow of list of strategies containing any of the specified tags
     */
    fun getStrategiesWithTags(tags: List<String>): Flow<List<Strategy>>

    /**
     * Activates a strategy by ID.
     * @param id The strategy ID to activate
     * @return True if activated successfully, false otherwise
     */
    suspend fun activateStrategy(id: String): Boolean

    /**
     * Deactivates a strategy by ID.
     * @param id The strategy ID to deactivate
     * @return True if deactivated successfully, false otherwise
     */
    suspend fun deactivateStrategy(id: String): Boolean

    /**
     * Gets the count of strategies.
     * @return The total number of strategies
     */
    suspend fun getStrategyCount(): Int

    /**
     * Gets the count of active strategies.
     * @return The number of active strategies
     */
    suspend fun getActiveStrategyCount(): Int
} 