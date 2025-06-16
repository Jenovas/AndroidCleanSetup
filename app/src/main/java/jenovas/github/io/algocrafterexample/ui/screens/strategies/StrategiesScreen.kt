package jenovas.github.io.algocrafterexample.ui.screens.strategies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Activity
import com.composables.icons.lucide.ChartBarBig
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Power
import com.composables.icons.lucide.PowerOff
import com.composables.icons.lucide.RefreshCw
import com.composables.icons.lucide.Tag
import com.composables.icons.lucide.Trash2
import com.composables.icons.lucide.TrendingDown
import com.composables.icons.lucide.TrendingUp
import jenovas.github.io.algocrafterexample.ui.components.ElevationLevel
import jenovas.github.io.algocrafterexample.ui.components.SharedButtonWithIcon
import jenovas.github.io.algocrafterexample.ui.components.SharedCard
import jenovas.github.io.algocrafterexample.ui.components.SharedScaffold
import jenovas.github.io.algocrafterexample.ui.navigation.ObserveAsEvents
import jenovas.github.io.algocrafterexample.ui.theme.AlgoCrafterExampleTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun StrategiesScreen(
    navController: NavHostController,
    viewModel: StrategiesViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            is StrategiesEffect.ShowMessage -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
            is StrategiesEffect.NavigateToDetail -> {
                // Navigate to strategy detail
            }
            is StrategiesEffect.NavigateToCreate -> {
                // Navigate to create strategy
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    StrategiesScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun StrategiesScreenContent(
    state: StrategiesUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (StrategiesEvent) -> Unit
) {
    SharedScaffold(
        topBarTitle = "My Strategies",
        snackbarHostState = snackbarHostState,
    ) {
        StrategiesScreenSharedScaffoldContent(
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
private fun StrategiesScreenSharedScaffoldContent(
    state: StrategiesUiState,
    onEvent: (StrategiesEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        StrategiesHeaderCard(
            totalCount = state.strategies.size,
            activeCount = state.activeStrategiesCount,
            onCreateClick = { onEvent(StrategiesEvent.NavigateToCreateStrategy) },
            onRefreshClick = { onEvent(StrategiesEvent.RefreshStrategies) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            state.isLoading -> {
                LoadingContent()
            }
            state.error != null -> {
                ErrorContent(
                    error = state.error,
                    onRetry = { onEvent(StrategiesEvent.LoadStrategies) },
                    onDismiss = { onEvent(StrategiesEvent.DismissError) }
                )
            }
            state.hasStrategies -> {
                StrategiesList(
                    strategies = state.filteredStrategies,
                    onStrategyClick = { strategyId ->
                        onEvent(StrategiesEvent.NavigateToStrategyDetail(strategyId))
                    },
                    onDeleteClick = { strategyId, strategyName ->
                        onEvent(StrategiesEvent.ShowDeleteConfirmation(strategyId, strategyName))
                    },
                    onToggleActiveClick = { strategyId ->
                        onEvent(StrategiesEvent.ToggleStrategyActive(strategyId))
                    }
                )
            }
            else -> {
                EmptyStrategiesContent(
                    onCreateClick = { onEvent(StrategiesEvent.NavigateToCreateStrategy) }
                )
            }
        }

        state.deleteConfirmation?.let { confirmation ->
            if (confirmation.isVisible) {
                DeleteConfirmationDialog(
                    strategyName = confirmation.strategyName,
                    onConfirm = { onEvent(StrategiesEvent.ConfirmDelete) },
                    onDismiss = { onEvent(StrategiesEvent.DismissDeleteConfirmation) }
                )
            }
        }
    }
}

@Composable
private fun StrategiesHeaderCard(
    totalCount: Int,
    activeCount: Int,
    onCreateClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    SharedCard(
        modifier = Modifier.fillMaxWidth(),
        elevationLevel = ElevationLevel.Elevation2,
        onEvent = { }
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Portfolio Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Lucide.ChartBarBig,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$totalCount strategies",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Lucide.Activity,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (activeCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$activeCount active",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (activeCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (activeCount > 0) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SharedButtonWithIcon(
                    text = "Create New",
                    imageVector = Lucide.Plus,
                    onClick = onCreateClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    imageTint = MaterialTheme.colorScheme.onPrimary
                )

                SharedButtonWithIcon(
                    text = "Refresh",
                    imageVector = Lucide.RefreshCw,
                    onClick = onRefreshClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    imageTint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun StrategiesList(
    strategies: List<StrategyItem>,
    onStrategyClick: (String) -> Unit,
    onDeleteClick: (String, String) -> Unit,
    onToggleActiveClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(strategies) { strategy ->
            StrategyCard(
                strategy = strategy,
                onClick = { onStrategyClick(strategy.id) },
                onDeleteClick = { onDeleteClick(strategy.id, strategy.name) },
                onToggleActiveClick = { onToggleActiveClick(strategy.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StrategyCard(
    strategy: StrategyItem,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleActiveClick: () -> Unit
) {
    SharedCard(
        modifier = Modifier.fillMaxWidth(),
        elevationLevel = ElevationLevel.Elevation1,
        onEvent = onClick
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strategy.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = strategy.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (strategy.isActive) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (strategy.isActive) Lucide.Power else Lucide.PowerOff,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = if (strategy.isActive) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (strategy.isActive) "ACTIVE" else "INACTIVE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (strategy.isActive) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = when(strategy.strategyType) {
                            "TREND_FOLLOWING" -> Lucide.TrendingUp
                            "MEAN_REVERSION" -> Lucide.TrendingDown
                            else -> Lucide.ChartBarBig
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Type",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = strategy.strategyType.replace("_", " "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (strategy.tags.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Lucide.Tag,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Tags",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = strategy.tags.take(2).joinToString(", "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    onClick = onToggleActiveClick,
                    shape = MaterialTheme.shapes.medium,
                    color = if (strategy.isActive) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (strategy.isActive) 1.0f else 0.7f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (strategy.isActive) Lucide.Power else Lucide.PowerOff,
                            contentDescription = if (strategy.isActive) "Deactivate strategy" else "Activate strategy",
                            modifier = Modifier.size(18.dp),
                            tint = if (strategy.isActive) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (strategy.isActive) "Active" else "Activate",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (strategy.isActive) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    onClick = onDeleteClick,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                    modifier = Modifier.alpha(0.8f)
                ) {
                    Box(
                        modifier = Modifier.padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Lucide.Trash2,
                            contentDescription = "Delete strategy",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading strategies...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    SharedCard(
        modifier = Modifier.fillMaxWidth(),
        elevationLevel = ElevationLevel.Elevation1,
        onEvent = { }
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error Loading Strategies",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SharedButtonWithIcon(
                    text = "Retry",
                    imageVector = Lucide.RefreshCw,
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    imageTint = MaterialTheme.colorScheme.onPrimary
                )
                SharedButtonWithIcon(
                    text = "Dismiss",
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
private fun EmptyStrategiesContent(
    onCreateClick: () -> Unit
) {
    SharedCard(
        modifier = Modifier.fillMaxWidth(),
        elevationLevel = ElevationLevel.Elevation1,
        onEvent = { }
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Lucide.ChartBarBig,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Strategies Yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create your first trading strategy to get started with algorithmic trading",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            SharedButtonWithIcon(
                text = "Create Your First Strategy",
                imageVector = Lucide.Plus,
                onClick = onCreateClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                imageTint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    strategyName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Strategy",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Are you sure you want to delete the strategy:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"$strategyName\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This action cannot be undone.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Delete",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview
@Composable
private fun DeleteConfirmationDialogPreview() {
    AlgoCrafterExampleTheme {
        DeleteConfirmationDialog(
            strategyName = "Moving Average Crossover",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun StrategiesScreenContentPreview() {
    AlgoCrafterExampleTheme {
        StrategiesScreenSharedScaffoldContent(
            state = StrategiesUiState(
                isLoading = false,
                error = null,
                strategies = listOf(),
            ),
            onEvent = {}
        )
    }
}