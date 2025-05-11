package jenovas.github.io.algocrafterexample.di

import jenovas.github.io.algocrafterexample.ui.screen.clean_setup_effects.CleanSetupEffectsViewModel
import jenovas.github.io.algocrafterexample.ui.screen.clean_setup_no_effects.CleanSetupViewModel
import jenovas.github.io.algocrafterexample.ui.screen.home.HomeScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CleanSetupViewModel)
    viewModelOf(::CleanSetupEffectsViewModel)
    viewModelOf(::HomeScreenViewModel)
}  