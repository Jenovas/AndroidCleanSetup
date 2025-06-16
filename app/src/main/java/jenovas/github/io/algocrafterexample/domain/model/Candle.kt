package jenovas.github.io.algocrafterexample.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Represents a single candlestick (OHLCV) data point for market data.
 * This is used for charting and technical analysis in trading strategies.
 */
data class Candle(
    val symbol: String,
    val timestamp: LocalDateTime,
    val open: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
    val close: BigDecimal,
    val volume: Long,
    val interval: TimeInterval
) {
    
    init {
        require(symbol.isNotBlank()) { "Symbol cannot be blank" }
        require(open >= BigDecimal.ZERO) { "Open price cannot be negative" }
        require(high >= BigDecimal.ZERO) { "High price cannot be negative" }
        require(low >= BigDecimal.ZERO) { "Low price cannot be negative" }
        require(close >= BigDecimal.ZERO) { "Close price cannot be negative" }
        require(volume >= 0) { "Volume cannot be negative" }
        require(high >= low) { "High price must be greater than or equal to low price" }
        require(high >= open) { "High price must be greater than or equal to open price" }
        require(high >= close) { "High price must be greater than or equal to close price" }
        require(low <= open) { "Low price must be less than or equal to open price" }
        require(low <= close) { "Low price must be less than or equal to close price" }
    }

    /**
     * Calculates the body size of the candle (absolute difference between open and close).
     */
    fun bodySize(): BigDecimal {
        return (close - open).abs()
    }

    /**
     * Calculates the full range of the candle (high - low).
     */
    fun range(): BigDecimal {
        return high - low
    }

    /**
     * Calculates the upper shadow length (distance from high to the higher of open/close).
     */
    fun upperShadow(): BigDecimal {
        val bodyTop = maxOf(open, close)
        return high - bodyTop
    }

    /**
     * Calculates the lower shadow length (distance from the lower of open/close to low).
     */
    fun lowerShadow(): BigDecimal {
        val bodyBottom = minOf(open, close)
        return bodyBottom - low
    }

    /**
     * Determines if this is a bullish candle (close > open).
     */
    fun isBullish(): Boolean {
        return close > open
    }

    /**
     * Determines if this is a bearish candle (close < open).
     */
    fun isBearish(): Boolean {
        return close < open
    }

    /**
     * Determines if this is a doji candle (open == close or very small body).
     */
    fun isDoji(threshold: BigDecimal = BigDecimal("0.01")): Boolean {
        return bodySize() <= threshold
    }

    /**
     * Gets the percentage change from open to close.
     */
    fun percentageChange(): BigDecimal {
        if (open == BigDecimal.ZERO) return BigDecimal.ZERO
        return ((close - open) / open) * BigDecimal("100")
    }
}

/**
 * Represents different time intervals for candle data.
 */
enum class TimeInterval(val displayName: String, val minutes: Int) {
    ONE_MINUTE("1m", 1),
    FIVE_MINUTES("5m", 5),
    FIFTEEN_MINUTES("15m", 15),
    THIRTY_MINUTES("30m", 30),
    ONE_HOUR("1h", 60),
    FOUR_HOURS("4h", 240),
    ONE_DAY("1d", 1440),
    ONE_WEEK("1w", 10080),
    ONE_MONTH("1M", 43200)
} 