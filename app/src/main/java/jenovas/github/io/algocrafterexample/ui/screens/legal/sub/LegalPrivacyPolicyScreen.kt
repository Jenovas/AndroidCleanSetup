package jenovas.github.io.algocrafterexample.ui.screens.legal.sub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import jenovas.github.io.algocrafterexample.ui.components.SharedScaffold
import jenovas.github.io.algocrafterexample.ui.navigation.LegalScreens
import jenovas.github.io.algocrafterexample.ui.navigation.ObserveAsEvents
import jenovas.github.io.algocrafterexample.ui.navigation.safeNavigateUp
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalBulletPoint
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalHeading
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalScreenContentState
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalScreenEffect
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalScreenEvent
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalSection
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PrivacyPolicyScreen(
    navController: NavHostController,
    viewModel: LegalViewModel = koinViewModel(viewModelStoreOwner = navController.getBackStackEntry<LegalScreens>())
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            is LegalScreenEffect.NavigateBack -> navController.safeNavigateUp()
            else -> {}
        }
    }

    PrivacyPolicyScreenContent(
        state.contentState,
        viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrivacyPolicyScreenContent(
    state: LegalScreenContentState,
    onEvent: (LegalScreenEvent) -> Unit,
) {
    val scrollState = rememberScrollState()
    SharedScaffold(
        topBarTitle = "Privacy Policy",
        topBarImageVector = Lucide.ArrowLeft,
        topBarOnEvent = { onEvent(LegalScreenEvent.NavigateBack) },
        bottomBarTitle = "Accept and Continue",
        bottomBarEnabled = true,
        bottomBarOnEvent = {
            onEvent(LegalScreenEvent.SetPrivacyAccepted(true))
            onEvent(LegalScreenEvent.NavigateBack)
        }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            PrivacyPolicyScreenLegalSection()
        }
    }
}

@Composable
private fun PrivacyPolicyScreenLegalSection() {
    LegalHeading("Privacy Policy")

    LegalSection(
        title = "Information We Collect",
        content = "AlgoCrafter collects certain information to provide and improve our services. This information includes:",
        number = 1,
        bottomSpacing = 8
    )

    LegalBulletPoint("Strategy Data: Information about the trading strategies you create, including indicators, parameters, and backtest results.")

    LegalBulletPoint("Usage Data: Information about how you use the app, including features accessed and actions taken.")

    LegalBulletPoint("Device Information: Basic information about your device, such as operating system version and device model.")

    Spacer(modifier = Modifier.height(16.dp))

    LegalSection(
        title = "How We Use Your Information",
        content = "We use the collected information for various purposes, including:",
        number = 2,
        bottomSpacing = 8
    )

    LegalBulletPoint("To provide and maintain our service")

    LegalBulletPoint("To improve and personalize your experience")

    LegalBulletPoint("To analyze usage patterns and optimize our app")

    Spacer(modifier = Modifier.height(16.dp))

    LegalSection(
        title = "Data Security",
        content = "The security of your data is important to us. While we strive to use commercially acceptable means to protect your personal information, we cannot guarantee its absolute security. All strategy data is stored locally on your device by default, unless you explicitly choose to use cloud features.",
        number = 3
    )

    LegalSection(
        title = "Third-Party Services",
        content = "Our app may use third-party services that collect information used to identify you. We use these services for AI-powered strategy analysis and market data retrieval.",
        number = 4
    )

    LegalSection(
        title = "Changes to This Privacy Policy",
        content = "We may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this screen. You are advised to review this Privacy Policy periodically for any changes.",
        number = 5
    )

    LegalSection(
        title = "Contact Us",
        content = "If you have any questions about this Privacy Policy, please contact us via email at support@algocrafter.app",
        number = 6
    )

    Spacer(modifier = Modifier.height(24.dp))
} 