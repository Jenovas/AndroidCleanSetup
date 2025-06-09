package jenovas.github.io.algocrafterexample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Observes a flow as events, collecting only when the lifecycle is at least at STARTED state.
 * This is useful for navigation events, snackbar messages, etc.
 */
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

/**
 * Navigation extension to navigate to a destination, popping up to the start destination
 * and removing all instances of the current destination from the back stack.
 * This effectively replaces the current screen with the new one.
 */
fun NavHostController.navigateWithPopUp(route: String, popUpToRoute: String) {
    this.navigate(route) {
        popUpTo(popUpToRoute) {
            inclusive = true
        }
    }
}

/**
 * Navigation extension to clear the entire back stack and navigate to the given route.
 * This is useful for logout or similar flows where you want to start from scratch.
 */
fun NavHostController.navigateWithClearBackstack(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateWithClearBackstack.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}

/**
 * Navigation extension to navigate to a destination, clearing all destinations on the back stack
 * except the start destination.
 * This is useful when you want to navigate back to the main flow of the app.
 */
fun NavHostController.navigateBackToStartDestination(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateBackToStartDestination.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Safely navigates up in the navigation hierarchy.
 * If there are no destinations on the back stack, this will do nothing.
 */
fun NavController.safeNavigateUp(): Boolean {
    return if (previousBackStackEntry != null) {
        navigateUp()
        true
    } else {
        false
    }
} 