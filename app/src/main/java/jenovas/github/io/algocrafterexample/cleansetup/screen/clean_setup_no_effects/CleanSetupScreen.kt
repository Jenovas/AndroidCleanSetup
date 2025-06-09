package jenovas.github.io.algocrafterexample.cleansetup.screen.clean_setup_no_effects

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import jenovas.github.io.algocrafterexample.cleansetup.navigation.CleanSetupHome
import jenovas.github.io.algocrafterexample.ui.theme.AlgoCrafterExampleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CleanSetupScreen(
    navController: NavHostController,
    viewModel: CleanSetupViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navTarget) {
        state.navTarget.let { target ->
            when (target) {
                CleanSetupNavTarget.None -> Unit
                CleanSetupNavTarget.Back -> navController.navigateUp()
                CleanSetupNavTarget.Home -> navController.navigate(CleanSetupHome)
            }
            viewModel.handleEvent(CleanSetupScreenEvent.NavigationHandled)
        }
    }

    CleanSetupScreenContent(
        state = state.contentState,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun CleanSetupScreenContent(
    state: CleanSetupScreenContentState,
    onEvent: (CleanSetupScreenEvent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage.let { snackbar ->
            if (snackbar.isBlank()) return@let
            snackbarHostState.showSnackbar(
                message = state.snackbarMessage
            )
            onEvent(CleanSetupScreenEvent.SnackbarShown)
        }
    }

    Scaffold(
        topBar = { CleanSetupTopBar() },
        bottomBar = { CleanSetupBottomBar(onEvent) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CleanSetupLoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.cleanSetupDataClassList.isNotEmpty()) {
                CleanSetupContentList(
                    state = state,
                    onEvent = onEvent
                )
            } else {
                CleanSetupContentEmptyList(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CleanSetupContentList(
    state: CleanSetupScreenContentState,
    onEvent: (CleanSetupScreenEvent) -> Unit
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
            CleanSetupContentListItem(
                item = item,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun CleanSetupContentListItem(
    item: CleanSetupDataClass,
    onEvent: (CleanSetupScreenEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(CleanSetupScreenEvent.ToggleFavourite(item))
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
private fun CleanSetupBottomBar(event: (CleanSetupScreenEvent) -> Unit) {
    Button(
        onClick = { event(CleanSetupScreenEvent.NavigateToHome) },
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
private fun CleanSetupTopBar() {
    TopAppBar(
        title = { Text("Clean Setup") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}

@Composable
private fun CleanSetupContentEmptyList(modifier: Modifier) {
    Text(
        text = "No data available",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier
    )
}

@Composable
private fun CleanSetupLoadingIndicator(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 4.dp
    )
}

@Preview()
@Composable
fun CleanSetupScreenLoadingPreview() {
    AlgoCrafterExampleTheme {
        CleanSetupScreenContent(
            state = CleanSetupScreenContentState(isLoading = true),
            onEvent = {},
        )
    }
}

@Preview()
@Composable
fun CleanSetupScreenEmptyPreview() {
    AlgoCrafterExampleTheme {
        CleanSetupScreenContent(
            state = CleanSetupScreenContentState(
                isLoading = false,
                cleanSetupDataClassList = emptyList()
            ),
            onEvent = {}
        )
    }
}

@Preview()
@Composable
fun CleanSetupScreenListPreview() {
    AlgoCrafterExampleTheme {
        CleanSetupScreenContent(
            state = CleanSetupScreenContentState(
                isLoading = false,
                cleanSetupDataClassList = CleanSetupDataClass.fakeList()
            ),
            onEvent = {}
        )
    }
}