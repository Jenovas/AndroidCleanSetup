package jenovas.github.io.algocrafterexample.cleansetup.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_effects.CleanSetupEffectsScreen
import jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_home.CleanSetupHomeScreen
import jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_no_effects.CleanSetupScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun CleanSetupAppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CleanSetupHome,
        modifier = modifier
    ) {
        composable<CleanSetupHome> {
            CleanSetupHomeScreen(navController)
        }

        composable<CleanSetupNoEffects> {
            CleanSetupScreen(navController)
        }

        composable<CleanSetupWithEffects> {
            CleanSetupEffectsScreen(navController)
        }
    }
}

@Composable
fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}