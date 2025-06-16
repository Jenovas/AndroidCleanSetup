package jenovas.github.io.algocrafterexample.domain.usecase.marketdata

import jenovas.github.io.algocrafterexample.domain.model.MarketData
import jenovas.github.io.algocrafterexample.domain.model.TimeInterval
import jenovas.github.io.algocrafterexample.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Use case for retrieving market data.
 * Encapsulates business logic for getting market data with validation and caching logic.
 */
class GetMarketDataUseCase(
    private val marketDataRepository: MarketDataRepository
) {

    /**
     * Gets market data for a specific symbol and time range.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param startTime The start time for data range
     * @param endTime The end time for data range
     * @return Flow of market data
     * @throws IllegalArgumentException if parameters are invalid
     */
    suspend operator fun invoke(
        symbol: String,
        interval: TimeInterval,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Flow<MarketData?> {
        validateParameters(symbol, startTime, endTime)
        
        return marketDataRepository.getMarketData(symbol, interval, startTime, endTime)
    }

    /**
     * Gets the latest market data for a symbol.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param candleCount The number of recent candles to retrieve
     * @return Flow of market data
     */
    suspend fun getLatestMarketData(
        symbol: String,
        interval: TimeInterval,
        candleCount: Int = 100
    ): Flow<MarketData?> {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        require(candleCount > 0) { "Candle count must be positive" }
        require(candleCount <= 1000) { "Candle count cannot exceed 1000" }
        
        return marketDataRepository.getLatestMarketData(symbol, interval, candleCount)
    }

    /**
     * Gets market data for the last N days.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param days The number of days to look back
     * @return Flow of market data
     */
    suspend fun getMarketDataForLastDays(
        symbol: String,
        interval: TimeInterval,
        days: Int
    ): Flow<MarketData?> {
        require(days > 0) { "Days must be positive" }
        require(days <= 365) { "Cannot retrieve more than 365 days of data" }
        
        val endTime = LocalDateTime.now()
        val startTime = endTime.minusDays(days.toLong())
        
        return invoke(symbol, interval, startTime, endTime)
    }

    /**
     * Gets cached market data without making network requests.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @return Cached market data if available, null otherwise
     */
    suspend fun getCachedMarketData(
        symbol: String,
        interval: TimeInterval
    ): MarketData? {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        
        return marketDataRepository.getCachedMarketData(symbol, interval)
    }

    /**
     * Checks if market data is available for a symbol.
     * @param symbol The financial instrument symbol
     * @return True if data is available, false otherwise
     */
    suspend fun isMarketDataAvailable(symbol: String): Boolean {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        
        return marketDataRepository.isSymbolAvailable(symbol)
    }

    /**
     * Gets available symbols for market data.
     * @return List of available symbols
     */
    suspend fun getAvailableSymbols(): List<String> {
        return marketDataRepository.getAvailableSymbols()
    }

    /**
     * Gets supported time intervals for a symbol.
     * @param symbol The financial instrument symbol
     * @return List of supported time intervals
     */
    suspend fun getSupportedIntervals(symbol: String): List<TimeInterval> {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        
        return marketDataRepository.getSupportedIntervals(symbol)
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