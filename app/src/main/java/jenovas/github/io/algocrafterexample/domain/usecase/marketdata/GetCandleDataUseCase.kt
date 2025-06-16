package jenovas.github.io.algocrafterexample.domain.usecase.marketdata

import jenovas.github.io.algocrafterexample.domain.model.Candle
import jenovas.github.io.algocrafterexample.domain.model.TimeInterval
import jenovas.github.io.algocrafterexample.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

/**
 * Use case for retrieving individual candle data.
 * Encapsulates business logic for getting candle-level market data and real-time updates.
 */
class GetCandleDataUseCase(
    private val marketDataRepository: MarketDataRepository
) {

    /**
     * Gets real-time candle updates for a symbol.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @return Flow of individual candles as they arrive
     * @throws IllegalArgumentException if symbol is blank
     */
    fun getRealTimeCandles(
        symbol: String,
        interval: TimeInterval
    ): Flow<Candle> {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        
        return marketDataRepository.getRealTimeCandles(symbol, interval)
    }

    /**
     * Gets the latest candles for a symbol.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param count The number of recent candles to retrieve
     * @return Flow of list of candles
     */
    fun getLatestCandles(
        symbol: String,
        interval: TimeInterval,
        count: Int = 50
    ): Flow<List<Candle>> {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        require(count > 0) { "Count must be positive" }
        require(count <= 500) { "Count cannot exceed 500" }
        
        return marketDataRepository.getLatestMarketData(symbol, interval, count)
            .map { marketData -> marketData?.candles ?: emptyList() }
    }

    /**
     * Gets candles within a specific time range.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param startTime The start time for data range
     * @param endTime The end time for data range
     * @return Flow of list of candles
     */
    fun getCandlesInRange(
        symbol: String,
        interval: TimeInterval,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Flow<List<Candle>> {
        validateTimeRange(startTime, endTime)
        
        return marketDataRepository.getMarketData(symbol, interval, startTime, endTime)
            .map { marketData -> marketData?.candles ?: emptyList() }
    }

    /**
     * Gets candles for technical analysis (with minimum required count).
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param minCandlesRequired Minimum number of candles needed for analysis
     * @return Flow of list of candles, empty if insufficient data
     */
    fun getCandlesForAnalysis(
        symbol: String,
        interval: TimeInterval,
        minCandlesRequired: Int = 20
    ): Flow<List<Candle>> {
        require(minCandlesRequired > 0) { "Minimum candles required must be positive" }
        
        return getLatestCandles(symbol, interval, minCandlesRequired * 2)
            .map { candles ->
                if (candles.size >= minCandlesRequired) {
                    candles.takeLast(minCandlesRequired)
                } else {
                    emptyList()
                }
            }
    }

    /**
     * Gets only bullish candles from the latest data.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param count The number of recent candles to check
     * @return Flow of list of bullish candles
     */
    fun getBullishCandles(
        symbol: String,
        interval: TimeInterval,
        count: Int = 50
    ): Flow<List<Candle>> {
        return getLatestCandles(symbol, interval, count)
            .map { candles -> candles.filter { it.isBullish() } }
    }

    /**
     * Gets only bearish candles from the latest data.
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param count The number of recent candles to check
     * @return Flow of list of bearish candles
     */
    fun getBearishCandles(
        symbol: String,
        interval: TimeInterval,
        count: Int = 50
    ): Flow<List<Candle>> {
        return getLatestCandles(symbol, interval, count)
            .map { candles -> candles.filter { it.isBearish() } }
    }

    /**
     * Gets candles with high volume (above average).
     * @param symbol The financial instrument symbol
     * @param interval The time interval for candles
     * @param count The number of recent candles to analyze
     * @return Flow of list of high volume candles
     */
    fun getHighVolumeCandles(
        symbol: String,
        interval: TimeInterval,
        count: Int = 50
    ): Flow<List<Candle>> {
        return getLatestCandles(symbol, interval, count)
            .map { candles ->
                if (candles.isEmpty()) return@map emptyList()
                
                val averageVolume = candles.map { it.volume }.average()
                candles.filter { it.volume > averageVolume }
            }
    }

    private fun validateTimeRange(startTime: LocalDateTime, endTime: LocalDateTime) {
        require(startTime.isBefore(endTime)) { "Start time must be before end time" }
        
        val now = LocalDateTime.now()
        require(!endTime.isAfter(now)) { "End time cannot be in the future" }
        
        val maxRangeDays = 30L
        val maxEndTime = startTime.plusDays(maxRangeDays)
        require(!endTime.isAfter(maxEndTime)) { 
            "Time range cannot exceed $maxRangeDays days" 
        }
    }
} 