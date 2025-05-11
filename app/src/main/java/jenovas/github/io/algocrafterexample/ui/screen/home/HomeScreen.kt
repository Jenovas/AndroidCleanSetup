package jenovas.github.io.algocrafterexample.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import jenovas.github.io.algocrafterexample.ui.navigation.CleanSetupNoEffects
import jenovas.github.io.algocrafterexample.ui.navigation.CleanSetupWithEffects
import jenovas.github.io.algocrafterexample.ui.theme.AlgoCrafterExampleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navTarget) {
        state.navTarget.let { target ->
            when (target) {
                HomeScreenNavTarget.None -> Unit
                HomeScreenNavTarget.Back -> navController.navigateUp()
                HomeScreenNavTarget.CleanSetupNoEffects -> navController.navigate(
                    CleanSetupNoEffects
                )

                HomeScreenNavTarget.CleanSetupWithEffects -> navController.navigate(
                    CleanSetupWithEffects
                )
            }
            viewModel.handleEvent(HomeScreenEvent.NavigationHandled)
        }
    }

    HomeScreenContent(
        state.contentState,
        viewModel::handleEvent
    )
}

@Composable
fun HomeScreenContent(
    contentState: HomeScreenContentState,
    event: (HomeScreenEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        }
    ) { paddingValues ->
        HomeScreenContentBox(
            modifier = Modifier
                .padding(paddingValues),
            event = event
        )
    }
}

@Composable
fun HomeScreenContentBox(
    modifier: Modifier,
    event: (HomeScreenEvent) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to AlgoCrafter",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your complete solution for algorithmic challenges",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { event(HomeScreenEvent.NavigateToCleanSetupNoEffects) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Go to Clean Setup No Effects",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = { event(HomeScreenEvent.NavigateToCleanSetupWithEffects) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Go to Clean Setup With Effects",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar() {
    TopAppBar(
        title = { Text("AlgoCrafter") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AlgoCrafterExampleTheme {
        HomeScreenContent(
            HomeScreenContentState(),
            {},
        )
    }
} 