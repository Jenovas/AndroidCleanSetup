package jenovas.github.io.algocrafterexample.di

import jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_effects.CleanSetupEffectsViewModel
import jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_home.CleanSetupHomeScreenViewModel
import jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_no_effects.CleanSetupViewModel
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalViewModel
import jenovas.github.io.algocrafterexample.ui.screens.menu.MenuViewModel
import jenovas.github.io.algocrafterexample.ui.screens.strategies.StrategiesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CleanSetupViewModel)
    viewModelOf(::CleanSetupEffectsViewModel)
    viewModelOf(::CleanSetupHomeScreenViewModel)
    viewModelOf(::LegalViewModel)
    viewModelOf(::MenuViewModel)
    viewModelOf(::StrategiesViewModel)
}  