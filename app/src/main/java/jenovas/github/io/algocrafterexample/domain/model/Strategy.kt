package jenovas.github.io.algocrafterexample.domain.model

import java.time.LocalDateTime

/**
 * Represents a trading strategy in the domain layer.
 * This is the core business entity for strategies, containing all business logic and validation.
 */
data class Strategy(
    val id: String,
    val name: String,
    val description: String,
    val strategyType: StrategyType,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val parameters: Map<String, Any> = emptyMap(),
    val tags: List<String> = emptyList()
) {
    
    init {
        require(id.isNotBlank()) { "Strategy ID cannot be blank" }
        require(name.isNotBlank()) { "Strategy name cannot be blank" }
        require(name.length <= 100) { "Strategy name cannot exceed 100 characters" }
        require(description.length <= 500) { "Strategy description cannot exceed 500 characters" }
    }

    /**
     * Checks if the strategy has been recently modified (within the last 24 hours).
     */
    fun isRecentlyModified(): Boolean {
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24)
        return updatedAt.isAfter(twentyFourHoursAgo)
    }

    /**
     * Checks if the strategy is ready for backtesting.
     * A strategy is ready if it's active and has the necessary parameters.
     */
    fun isReadyForBacktest(): Boolean {
        return isActive && parameters.isNotEmpty()
    }

    /**
     * Creates a copy of the strategy with updated timestamp.
     */
    fun withUpdatedTimestamp(): Strategy {
        return copy(updatedAt = LocalDateTime.now())
    }

    /**
     * Creates a deactivated copy of the strategy.
     */
    fun deactivate(): Strategy {
        return copy(
            isActive = false,
            updatedAt = LocalDateTime.now()
        )
    }

    /**
     * Creates an activated copy of the strategy.
     */
    fun activate(): Strategy {
        return copy(
            isActive = true,
            updatedAt = LocalDateTime.now()
        )
    }
} 