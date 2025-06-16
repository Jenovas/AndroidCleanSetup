package jenovas.github.io.algocrafterexample.di

import jenovas.github.io.algocrafterexample.domain.repository.MockStrategyRepository
import jenovas.github.io.algocrafterexample.domain.repository.StrategyRepository
import jenovas.github.io.algocrafterexample.domain.usecase.strategy.DeleteStrategyUseCase
import jenovas.github.io.algocrafterexample.domain.usecase.strategy.GetStrategiesUseCase
import jenovas.github.io.algocrafterexample.domain.usecase.strategy.GetStrategyByIdUseCase
import jenovas.github.io.algocrafterexample.domain.usecase.strategy.SaveStrategyUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    // Repositories (will be replaced with real implementations in data layer)
    singleOf(::MockStrategyRepository) bind StrategyRepository::class
    
    // Strategy Use Cases
    factoryOf(::GetStrategiesUseCase)
    factoryOf(::SaveStrategyUseCase)
    factoryOf(::DeleteStrategyUseCase)
    factoryOf(::GetStrategyByIdUseCase)
} 