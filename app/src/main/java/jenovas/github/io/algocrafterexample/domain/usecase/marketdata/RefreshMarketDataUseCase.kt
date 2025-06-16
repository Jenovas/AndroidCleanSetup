package jenovas.github.io.algocrafterexample.domain.usecase.marketdata

import jenovas.github.io.algocrafterexample.domain.model.MarketData
import jenovas.github.io.algocrafterexample.domain.model.TimeInterval
import jenovas.github.io.algocrafterexample.domain.repository.MarketDataRepository
import java.time.LocalDateTime

/**
 * Use case for refreshing market data from remote sources.
 * Encapsulates business logic for data refresh, caching, and update strategies.
 */
class RefreshMarketDataUseCase(
    private val marketDataRepository: MarketDataRepository
) {

    /**
     * Refreshes market data for a specific symbol and time range.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param startTime The start time for data range
     * @param endTime The end time for data range
     * @return The refreshed market data, null if refresh failed
     * @throws IllegalArgumentException if parameters are invalid
     */
    suspend operator fun invoke(
        symbol: String,
        interval: TimeInterval,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): MarketData? {
        validateParameters(symbol, startTime, endTime)
        
        val refreshedData = marketDataRepository.refreshMarketData(symbol, interval, startTime, endTime)

        refreshedData?.let { data ->
            marketDataRepository.cacheMarketData(data)
        }
        
        return refreshedData
    }

    /**
     * Refreshes the latest market data for a symbol.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param days The number of days to refresh (default: 7)
     * @return The refreshed market data, null if refresh failed
     */
    suspend fun refreshLatestData(
        symbol: String,
        interval: TimeInterval,
        days: Int = 7
    ): MarketData? {
        require(days > 0) { "Days must be positive" }
        require(days <= 30) { "Cannot refresh more than 30 days at once" }
        
        val endTime = LocalDateTime.now()
        val startTime = endTime.minusDays(days.toLong())
        
        return invoke(symbol, interval, startTime, endTime)
    }

    /**
     * Refreshes market data only if it's stale (older than specified age).
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param maxAgeMinutes Maximum age in minutes before considering stale
     * @return The market data (refreshed if needed), null if refresh failed
     */
    suspend fun refreshIfStale(
        symbol: String,
        interval: TimeInterval,
        maxAgeMinutes: Int = 15
    ): MarketData? {
        require(maxAgeMinutes > 0) { "Max age minutes must be positive" }

        val needsUpdate = marketDataRepository.needsDataUpdate(symbol, interval, maxAgeMinutes)
        
        return if (needsUpdate) {
            refreshLatestData(symbol, interval)
        } else {
            marketDataRepository.getCachedMarketData(symbol, interval)
        }
    }

    /**
     * Forces a refresh of market data regardless of cache age.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @return The refreshed market data, null if refresh failed
     */
    suspend fun forceRefresh(
        symbol: String,
        interval: TimeInterval
    ): MarketData? {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }

        marketDataRepository.clearCachedData(symbol, interval)
        
        return refreshLatestData(symbol, interval)
    }

    /**
     * Refreshes multiple symbols at once.
     * @param symbols List of symbols to refresh
     * @param interval The time interval for candles
     * @param days The number of days to refresh for each symbol
     * @return Map of symbol to refreshed market data (null if refresh failed for that symbol)
     */
    suspend fun refreshMultipleSymbols(
        symbols: List<String>,
        interval: TimeInterval,
        days: Int = 7
    ): Map<String, MarketData?> {
        require(symbols.isNotEmpty()) { "Symbols list cannot be empty" }
        require(symbols.size <= 10) { "Cannot refresh more than 10 symbols at once" }
        require(symbols.all { it.isNotBlank() }) { "All symbols must be non-blank" }
        
        val results = mutableMapOf<String, MarketData?>()
        
        symbols.forEach { symbol ->
            try {
                results[symbol] = refreshLatestData(symbol, interval, days)
            } catch (e: Exception) {
                results[symbol] = null
            }
        }
        
        return results
    }

    /**
     * Gets the last update time for cached market data.
     * @param symbol The financial instrument symbol
     * @param interval The time interval
     * @return The last update time, null if no cached data
     */
    suspend fun getLastUpdateTime(
        symbol: String,
        interval: TimeInterval
    ): LocalDateTime? {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        
        return marketDataRepository.getLastUpdateTime(symbol, interval)
    }

    /**
     * Checks if market data needs updating.
     * @param symbol The financial instrument symbol
     * @param interval The time interval
     * @param maxAgeMinutes Maximum age in minutes before considering stale
     * @return True if data needs updating, false otherwise
     */
    suspend fun needsUpdate(
        symbol: String,
        interval: TimeInterval,
        maxAgeMinutes: Int = 15
    ): Boolean {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        require(maxAgeMinutes > 0) { "Max age minutes must be positive" }
        
        return marketDataRepository.needsDataUpdate(symbol, interval, maxAgeMinutes)
    }

    private fun validateParameters(
        symbol: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ) {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        require(startTime.isBefore(endTime)) { "Start time must be before end time" }
        
        val now = LocalDateTime.now()
        require(!endTime.isAfter(now)) { "End time cannot be in the future" }
        
        val maxHistoryDays = 365L
        val earliestAllowed = now.minusDays(maxHistoryDays)
        require(!startTime.isBefore(earliestAllowed)) { 
            "Start time cannot be more than $maxHistoryDays days ago" 
        }
    }
} 