package jenovas.github.io.algocrafterexample.domain.repository

import jenovas.github.io.algocrafterexample.domain.model.Candle
import jenovas.github.io.algocrafterexample.domain.model.MarketData
import jenovas.github.io.algocrafterexample.domain.model.TimeInterval
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Repository interface for market data operations.
 * This interface defines the contract for market data retrieval and caching,
 * following Clean Architecture principles by being part of the domain layer.
 */
interface MarketDataRepository {

    /**
     * Gets market data for a specific symbol and time interval.
     * @param symbol The financial instrument symbol (e.g., "AAPL", "BTC/USD")
     * @param interval The time interval for candles
     * @param startTime The start time for data range
     * @param endTime The end time for data range
     * @return Flow of market data
     */
    fun getMarketData(
        symbol: String,
        interval: TimeInterval,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Flow<MarketData?>

    /**
     * Gets the latest market data for a symbol with a specific interval.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param candleCount The number of recent candles to retrieve
     * @return Flow of market data
     */
    fun getLatestMarketData(
        symbol: String,
        interval: TimeInterval,
        candleCount: Int = 100
    ): Flow<MarketData?>

    /**
     * Gets real-time market data updates for a symbol.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @return Flow of individual candles as they arrive
     */
    fun getRealTimeCandles(
        symbol: String,
        interval: TimeInterval
    ): Flow<Candle>

    /**
     * Refreshes market data from the remote source.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param startTime The start time for data range
     * @param endTime The end time for data range
     * @return The refreshed market data
     */
    suspend fun refreshMarketData(
        symbol: String,
        interval: TimeInterval,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): MarketData?

    /**
     * Gets cached market data without making network requests.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @return Cached market data if available, null otherwise
     */
    suspend fun getCachedMarketData(
        symbol: String,
        interval: TimeInterval
    ): MarketData?

    /**
     * Caches market data locally.
     * @param marketData The market data to cache
     */
    suspend fun cacheMarketData(marketData: MarketData)

    /**
     * Gets available symbols for market data.
     * @return List of available symbols
     */
    suspend fun getAvailableSymbols(): List<String>

    /**
     * Checks if market data is available for a symbol.
     * @param symbol The financial instrument symbol
     * @return True if data is available, false otherwise
     */
    suspend fun isSymbolAvailable(symbol: String): Boolean

    /**
     * Gets supported time intervals for a symbol.
     * @param symbol The financial instrument symbol
     * @return List of supported time intervals
     */
    suspend fun getSupportedIntervals(symbol: String): List<TimeInterval>

    /**
     * Clears cached market data for a symbol.
     * @param symbol The financial instrument symbol
     * @param interval The time interval to clear (optional, clears all if null)
     */
    suspend fun clearCachedData(symbol: String, interval: TimeInterval? = null)

    /**
     * Gets the last update time for cached market data.
     * @param symbol The financial instrument symbol
     * @param interval The time interval
     * @return The last update time, null if no cached data
     */
    suspend fun getLastUpdateTime(
        symbol: String,
        interval: TimeInterval
    ): LocalDateTime?

    /**
     * Checks if cached market data needs updating.
     * @param symbol The financial instrument symbol
     * @param interval The time interval
     * @param maxAgeMinutes Maximum age in minutes before considering stale
     * @return True if data needs updating, false otherwise
     */
    suspend fun needsDataUpdate(
        symbol: String,
        interval: TimeInterval,
        maxAgeMinutes: Int = 15
    ): Boolean
} 