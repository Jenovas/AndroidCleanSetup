package jenovas.github.io.algocrafterexample.domain.model

/**
 * Represents different types of trading strategies supported by the application.
 * This enum categorizes strategies based on their core approach and methodology.
 */
enum class StrategyType {
    /**
     * Strategies that follow the trend of the market.
     * Examples: Moving Average Crossover, MACD, Momentum strategies
     */
    TREND_FOLLOWING,

    /**
     * Strategies that attempt to identify market reversals.
     * Examples: RSI Divergence, Bollinger Band Squeeze, Support/Resistance
     */
    MEAN_REVERSION,

    /**
     * Strategies that exploit price differences between related instruments.
     * Examples: Statistical Arbitrage, Pairs Trading
     */
    ARBITRAGE,

    /**
     * Strategies that trade based on momentum indicators.
     * Examples: Breakout strategies, Volume-based momentum
     */
    MOMENTUM,

    /**
     * Strategies that profit from range-bound markets.
     * Examples: Grid Trading, Channel Trading
     */
    RANGE_TRADING,

    /**
     * Custom or AI-generated strategies that don't fit standard categories.
     * Examples: Machine Learning based strategies, Custom indicators
     */
    CUSTOM
} 