package jenovas.github.io.algocrafterexample.ui.screens.legal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jenovas.github.io.algocrafterexample.R
import jenovas.github.io.algocrafterexample.ui.components.ElevationLevel
import jenovas.github.io.algocrafterexample.ui.components.SharedCard

@Composable
fun LegalScreenHeader() {
    Image(
        painter = painterResource(id = R.drawable.logonobg),
        contentDescription = "AlgoCrafter Logo",
        modifier = Modifier.size(128.dp),
        contentScale = ContentScale.Fit
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "Welcome to AlgoCrafter",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Before we begin, please review and accept our legal agreements to ensure a secure experience.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        lineHeight = 24.sp
    )
}

@Composable
fun LegalAgreementCard(
    title: String,
    description: String,
    isAccepted: Boolean,
    onAccept: () -> Unit,
    onNavigate: () -> Unit
) {
    SharedCard(
        modifier = Modifier
            .fillMaxWidth(),
        elevationLevel = ElevationLevel.Elevation1,
        onEvent = { onNavigate() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isAccepted) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    .clickable {
                        if (!isAccepted) onAccept()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isAccepted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Accepted",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LegalSection(
    title: String,
    content: String,
    number: Int? = null,
    bottomSpacing: Int = 24
) {
    Column(modifier = Modifier.padding(bottom = bottomSpacing.dp)) {
        Text(
            text = if (number != null) "$number. $title" else title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun LegalBulletPoint(
    text: String
) {
    Text(
        text = "• $text",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun LegalHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))
}