package jenovas.github.io.algocrafterexample.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Represents market data for a specific financial instrument over a time period.
 * Contains a series of candles and metadata about the market data.
 */
data class MarketData(
    val symbol: String,
    val candles: List<Candle>,
    val interval: TimeInterval,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val lastUpdated: LocalDateTime
) {
    
    init {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        require(startTime.isBefore(endTime) || startTime.isEqual(endTime)) { 
            "Start time must be before or equal to end time" 
        }
        require(candles.all { it.symbol == symbol }) { 
            "All candles must have the same symbol as market data" 
        }
        require(candles.all { it.interval == interval }) { 
            "All candles must have the same interval as market data" 
        }
    }

    /**
     * Gets the total number of candles in this market data.
     */
    fun candleCount(): Int = candles.size

    /**
     * Checks if the market data is empty (no candles).
     */
    fun isEmpty(): Boolean = candles.isEmpty()

    /**
     * Gets the latest candle (most recent).
     */
    fun latestCandle(): Candle? = candles.maxByOrNull { it.timestamp }

    /**
     * Gets the earliest candle (oldest).
     */
    fun earliestCandle(): Candle? = candles.minByOrNull { it.timestamp }

    /**
     * Gets the highest price across all candles.
     */
    fun highestPrice(): BigDecimal? = candles.maxOfOrNull { it.high }

    /**
     * Gets the lowest price across all candles.
     */
    fun lowestPrice(): BigDecimal? = candles.minOfOrNull { it.low }

    /**
     * Gets the total volume across all candles.
     */
    fun totalVolume(): Long = candles.sumOf { it.volume }

    /**
     * Gets the average volume across all candles.
     */
    fun averageVolume(): Long = if (candles.isEmpty()) 0 else totalVolume() / candles.size

    /**
     * Calculates the overall percentage change from first to last candle.
     */
    fun overallPercentageChange(): BigDecimal? {
        val first = earliestCandle()
        val last = latestCandle()
        
        return if (first != null && last != null && first.open > BigDecimal.ZERO) {
            ((last.close - first.open) / first.open) * BigDecimal("100")
        } else {
            null
        }
    }

    /**
     * Gets candles within a specific time range.
     */
    fun getCandlesInRange(
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Candle> {
        return candles.filter { candle ->
            !candle.timestamp.isBefore(start) && !candle.timestamp.isAfter(end)
        }
    }

    /**
     * Gets the most recent N candles.
     */
    fun getRecentCandles(count: Int): List<Candle> {
        return candles
            .sortedByDescending { it.timestamp }
            .take(count)
    }

    /**
     * Checks if the market data needs updating (older than specified minutes).
     */
    fun needsUpdate(maxAgeMinutes: Int): Boolean {
        val cutoffTime = LocalDateTime.now().minusMinutes(maxAgeMinutes.toLong())
        return lastUpdated.isBefore(cutoffTime)
    }

    /**
     * Creates a new MarketData with updated timestamp.
     */
    fun withUpdatedTimestamp(): MarketData {
        return copy(lastUpdated = LocalDateTime.now())
    }

    /**
     * Creates a new MarketData with additional candles appended.
     */
    fun withAdditionalCandles(newCandles: List<Candle>): MarketData {
        val allCandles = (candles + newCandles)
            .distinctBy { it.timestamp }
            .sortedBy { it.timestamp }
        
        return copy(
            candles = allCandles,
            lastUpdated = LocalDateTime.now(),
            endTime = allCandles.maxOfOrNull { it.timestamp } ?: endTime
        )
    }
} 