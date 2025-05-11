package jenovas.github.io.algocrafterexample.ui.screen.clean_setup_effects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import jenovas.github.io.algocrafterexample.ui.navigation.Home
import jenovas.github.io.algocrafterexample.ui.navigation.ObserveAsEvents
import jenovas.github.io.algocrafterexample.ui.theme.AlgoCrafterExampleTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CleanSetupEffectsScreen(
    navController: NavHostController,
    viewModel: CleanSetupEffectsViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            is CleanSetupEffect.NavigateBack -> navController.navigateUp()
            is CleanSetupEffect.NavigateToHome -> navController.navigate(Home)
            is CleanSetupEffect.ShowMessage -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    CleanSetupEffectsScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
private fun CleanSetupEffectsScreenContent(
    state: CleanSetupEffectsUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (CleanSetupEffectsScreenEvent) -> Unit,
) {
    Scaffold(
        topBar = { CleanSetupEffectsTopBar() },
        bottomBar = { CleanSetupEffectsBottomBar(onEvent) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CleanSetupEffectsLoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.cleanSetupDataClassList.isNotEmpty()) {
                CleanSetupEffectsContentList(
                    state = state,
                    onEvent = onEvent
                )
            } else {
                CleanSetupEffectsContentEmptyList(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CleanSetupEffectsContentList(
    state: CleanSetupEffectsUiState,
    onEvent: (CleanSetupEffectsScreenEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            count = state.cleanSetupDataClassList.size,
            key = { index -> state.cleanSetupDataClassList[index].name }
        ) { index ->
            val item = state.cleanSetupDataClassList[index]
            CleanSetupEffectsListItem(
                item = item,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun CleanSetupEffectsListItem(
    item: CleanSetupEffectsDataClass,
    onEvent: (CleanSetupEffectsScreenEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(CleanSetupEffectsScreenEvent.ToggleFavourite(item))
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(16.dp)

        )

        val tint = if (item.isFavourite)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurface

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Star",
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp),
            tint = tint
        )
    }
}

@Composable
private fun CleanSetupEffectsBottomBar(
    onEvent: (CleanSetupEffectsScreenEvent) -> Unit
) {
    Button(
        onClick = { onEvent(CleanSetupEffectsScreenEvent.NavigateToHome) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Continue",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CleanSetupEffectsTopBar() {
    TopAppBar(
        title = { Text("Clean Setup") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}

@Composable
private fun CleanSetupEffectsContentEmptyList(modifier: Modifier) {
    Text(
        text = "No data available",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier
    )
}

@Composable
private fun CleanSetupEffectsLoadingIndicator(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 4.dp
    )
}

@Preview()
@Composable
fun CleanSetupEffectsScreenLoadingPreview() {
    AlgoCrafterExampleTheme {
        CleanSetupEffectsScreenContent(
            state = CleanSetupEffectsUiState(isLoading = true),
            snackbarHostState = SnackbarHostState(),
            onEvent = {}
        )
    }
}

@Preview()
@Composable
fun CleanSetupEffectsScreenEmptyPreview() {
    AlgoCrafterExampleTheme {
        CleanSetupEffectsScreenContent(
            state = CleanSetupEffectsUiState(
                isLoading = false,
                cleanSetupDataClassList = emptyList()
            ),
            snackbarHostState = SnackbarHostState(),
            onEvent = {}
        )
    }
}

@Preview()
@Composable
fun CleanSetupEffectsScreenListPreview() {
    AlgoCrafterExampleTheme {
        CleanSetupEffectsScreenContent(
            state = CleanSetupEffectsUiState(
                isLoading = false,
                cleanSetupDataClassList = CleanSetupEffectsDataClass.fakeList()
            ),
            snackbarHostState = SnackbarHostState(),
            onEvent = {}
        )
    }
}