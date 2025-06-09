package jenovas.github.io.algocrafterexample.ui.screens.menu

import jenovas.github.io.algocrafterexample.ui.navigation.ObserveAsEvents
import jenovas.github.io.algocrafterexample.ui.screens.menu.components.AIPremiumFeatureCard
import jenovas.github.io.algocrafterexample.ui.screens.menu.components.AIStrategyBuilderCard
import jenovas.github.io.algocrafterexample.ui.screens.menu.components.AppVersionFooter
import jenovas.github.io.algocrafterexample.ui.screens.menu.components.MenuOptionCard
import jenovas.github.io.algocrafterexample.ui.screens.menu.components.MenuOptionItem
import jenovas.github.io.algocrafterexample.ui.screens.menu.components.WelcomeCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.composables.icons.lucide.ChartBarBig
import com.composables.icons.lucide.Database
import com.composables.icons.lucide.ListOrdered
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import jenovas.github.io.algocrafterexample.ui.components.SharedScaffold
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MenuScreen(
    navController: NavHostController,
    viewModel: MenuViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            is MenuEffect.Navigate<*> -> navController.navigate(effect.destination)
            is MenuEffect.ShowMessage -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    MenuScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun MenuScreenContent(
    state: MenuUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (MenuScreenEvent) -> Unit
) {
    SharedScaffold(
        topBarTitle = "AlgoCrafter",
        snackbarHostState = snackbarHostState,
    ) {
        MenuScreenSharedScaffoldContent(
            onEvent = onEvent
        )
    }
}

@Composable
private fun MenuScreenSharedScaffoldContent(
    onEvent: (MenuScreenEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val menuOptionItems = getMenuOptions(onEvent)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        WelcomeCard { onEvent(MenuScreenEvent.NavigateToCreateStrategy) }

        Spacer(modifier = Modifier.height(24.dp))

        AIStrategyBuilderCard(
            onNavigateToAIBuilder = { onEvent(MenuScreenEvent.NavigateToAIBuilder) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AIPremiumFeatureCard(
            onShowMessage = { message ->
                onEvent(MenuScreenEvent.ShowMessage(message))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        menuOptionItems.forEach { option ->
            MenuOptionCard(option)
            Spacer(modifier = Modifier.height(8.dp))
        }

        AppVersionFooter()
    }
}

private fun getMenuOptions(onEvent: (MenuScreenEvent) -> Unit): List<MenuOptionItem> {
    return listOf(
        MenuOptionItem(
            icon = Lucide.ListOrdered,
            title = "My Strategies",
            subtitle = "View and manage strategies",
            onClick = { onEvent(MenuScreenEvent.ShowMessage("My Strategies coming soon")) }
        ),
        MenuOptionItem(
            icon = Lucide.ChartBarBig,
            title = "Backtest Results",
            subtitle = "View previous backtests",
            onClick = { onEvent(MenuScreenEvent.ShowMessage("Backtest Results coming soon")) }
        ),
        MenuOptionItem(
            icon = Lucide.Database,
            title = "Market Data",
            subtitle = "Browse available market data",
            onClick = { onEvent(MenuScreenEvent.ShowMessage("Market Data coming soon")) }
        ),
        MenuOptionItem(
            icon = Lucide.Settings,
            title = "Settings",
            subtitle = "Configure app preferences",
            onClick = { onEvent(MenuScreenEvent.ShowMessage("Settings coming soon")) }
        )
    )
} 