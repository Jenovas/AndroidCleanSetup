package jenovas.github.io.algocrafterexample.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val BUTTON_HORIZONTAL_PADDING = 16.dp
val BUTTON_VERTICAL_PADDING = 16.dp
val BUTTON_VERTICAL_PADDING_EXTRA = 48.dp

@Composable
fun SharedScaffold(
    topBarTitle: String,
    topBarImageVector: ImageVector? = null,
    topBarOnEvent: () -> Unit = {},
    snackbarHostState: SnackbarHostState? = null,
    bottomBarTitle: String? = null,
    bottomBarEnabled: Boolean = true,
    bottomBarOnEvent: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            SharedTopBar(
                title = topBarTitle,
                imageVector = topBarImageVector,
                onEvent = topBarOnEvent
            )
        },
        snackbarHost = {
            if (snackbarHostState == null) return@Scaffold
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            if (bottomBarTitle == null) return@Scaffold
            SharedBottomButton(
                title = bottomBarTitle,
                enabled = bottomBarEnabled,
                onEvent = bottomBarOnEvent,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTopBar(
    title: String,
    imageVector: ImageVector? = null,
    onEvent: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold

            )
        },
        navigationIcon = {
            if (imageVector == null) return@TopAppBar
            IconButton(onClick = { onEvent() }) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun SharedBottomButton(
    title: String,
    enabled: Boolean = true,
    onEvent: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = BUTTON_HORIZONTAL_PADDING,
            )
            .padding(
                top = BUTTON_VERTICAL_PADDING / 2,
                bottom = if (true) BUTTON_VERTICAL_PADDING_EXTRA else BUTTON_VERTICAL_PADDING
            ),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SharedCard(
    modifier: Modifier = Modifier,
    elevationLevel: ElevationLevel = ElevationLevel.Elevation0,
    onEvent: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(elevationLevel.elevation),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevationLevel.elevation
        ),
        onClick = onEvent
    ) {
        content()
    }
}

@Composable
fun SharedButtonWithIcon(
    text: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    imageTint: Color = MaterialTheme.colorScheme.primary,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.primary
    ),
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = 2.dp,
        pressedElevation = 4.dp
    ),
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = colors,
        elevation = elevation,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = imageTint
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

sealed class ElevationLevel(val elevation: Dp) {
    object Elevation0 : ElevationLevel(0.dp)
    object Elevation1 : ElevationLevel(1.dp)
    object Elevation2 : ElevationLevel(2.dp)
    object Elevation3 : ElevationLevel(3.dp)
    object Elevation4 : ElevationLevel(4.dp)
    object Elevation5 : ElevationLevel(6.dp)
    object Elevation6 : ElevationLevel(8.dp)
    object Elevation7 : ElevationLevel(12.dp)
    object Elevation8 : ElevationLevel(16.dp)
    object Elevation9 : ElevationLevel(24.dp)
}