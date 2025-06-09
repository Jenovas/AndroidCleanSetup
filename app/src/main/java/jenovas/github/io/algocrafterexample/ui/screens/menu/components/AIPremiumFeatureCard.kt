package jenovas.github.io.algocrafterexample.ui.screens.menu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChartNoAxesCombined
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lock
import com.composables.icons.lucide.Lucide
import jenovas.github.io.algocrafterexample.ui.components.ElevationLevel
import jenovas.github.io.algocrafterexample.ui.components.SharedButtonWithIcon
import jenovas.github.io.algocrafterexample.ui.components.SharedCard
import jenovas.github.io.algocrafterexample.ui.theme.AlgoCrafterExampleTheme

@Composable
fun AIPremiumFeatureCard(
    onShowMessage: (String) -> Unit
) {
    SharedCard(
        modifier = Modifier.fillMaxWidth(),
        elevationLevel = ElevationLevel.Elevation2,
        onEvent = { },
    ) {
        Column(
            modifier = Modifier.padding(
                start = 24.dp,
                top = 24.dp,
                end = 24.dp,
                bottom = 24.dp
            )
        ) {
            Row {
                Icon(
                    imageVector = Lucide.ChartNoAxesCombined,
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .alpha(0.85f),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    "AI Trading Assistant",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "AI-powered strategy analysis and building capabilities",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SharedButtonWithIcon(
                    text = "Upgrade",
                    modifier = Modifier.weight(1f),
                    imageVector = Lucide.Lock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = { onShowMessage("This feature requires a premium subscription.") }
                )

                SharedButtonWithIcon(
                    text = "Learn More",
                    modifier = Modifier.weight(1f),
                    imageVector = Lucide.Info,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = { onShowMessage("Learn more about AI-powered features.") },
                )
            }
        }
    }
}

@Preview
@Composable
fun AIPremiumFeatureCardPreview() {
    AlgoCrafterExampleTheme {
        AIPremiumFeatureCard(
            onShowMessage = {}
        )
    }
}