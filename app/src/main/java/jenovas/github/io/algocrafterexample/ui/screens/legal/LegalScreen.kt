package jenovas.github.io.algocrafterexample.ui.screens.legal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import jenovas.github.io.algocrafterexample.ui.components.SharedScaffold
import jenovas.github.io.algocrafterexample.ui.navigation.AIDisclaimer
import jenovas.github.io.algocrafterexample.ui.navigation.LegalScreens
import jenovas.github.io.algocrafterexample.ui.navigation.Menu
import jenovas.github.io.algocrafterexample.ui.navigation.ObserveAsEvents
import jenovas.github.io.algocrafterexample.ui.navigation.PrivacyPolicy
import jenovas.github.io.algocrafterexample.ui.navigation.TermsOfService
import jenovas.github.io.algocrafterexample.ui.navigation.safeNavigateUp
import jenovas.github.io.algocrafterexample.ui.theme.AlgoCrafterExampleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LegalScreen(
    navController: NavHostController,
    viewModel: LegalViewModel = koinViewModel(viewModelStoreOwner = navController.getBackStackEntry<LegalScreens>())
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            is LegalScreenEffect.ShowMessage -> {}
            is LegalScreenEffect.NavigateToTermsOfService -> navController.navigate(TermsOfService)
            is LegalScreenEffect.NavigateToPrivacyPolicy -> navController.navigate(PrivacyPolicy)
            is LegalScreenEffect.NavigateToAIDisclaimer -> navController.navigate(AIDisclaimer)
            is LegalScreenEffect.NavigateBack -> navController.safeNavigateUp()
            is LegalScreenEffect.NavigateNext -> navController.navigate(Menu)
        }
    }

    LegalScreenContent(
        state = state.contentState,
        onEvent = viewModel::handleEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LegalScreenContent(
    state: LegalScreenContentState,
    onEvent: (LegalScreenEvent) -> Unit
) {
    SharedScaffold(
        topBarTitle = "Legal Agreements",
        bottomBarTitle = "Accept All and Continue",
        bottomBarEnabled = state.allAccepted,
        bottomBarOnEvent = { onEvent(LegalScreenEvent.SetAllAccepted) }
    ) {
        LegalScreenSharedScaffoldContent(
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
private fun LegalScreenSharedScaffoldContent(
    state: LegalScreenContentState,
    onEvent: (LegalScreenEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        LegalScreenHeader()

        Spacer(modifier = Modifier.height(32.dp))

        LegalAgreementCard(
            title = "Terms of Service",
            description = "Our terms governing the use of AlgoCrafter services and features.",
            isAccepted = state.tosAccepted,
            onAccept = { onEvent(LegalScreenEvent.SetTosAccepted(true)) },
            onNavigate = { onEvent(LegalScreenEvent.NavigateToTermsOfService) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LegalAgreementCard(
            title = "Privacy Policy",
            description = "How we collect, use, and protect your personal information.",
            isAccepted = state.privacyAccepted,
            onAccept = { onEvent(LegalScreenEvent.SetPrivacyAccepted(true)) },
            onNavigate = { onEvent(LegalScreenEvent.NavigateToPrivacyPolicy) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LegalAgreementCard(
            title = "AI Disclaimer",
            description = "Important information about our AI-powered features and limitations.",
            isAccepted = state.aiDisclaimerAccepted,
            onAccept = { onEvent(LegalScreenEvent.SetAIDisclaimerAccepted(true)) },
            onNavigate = { onEvent(LegalScreenEvent.NavigateToAIDisclaimer) }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun LegalScreenPreview() {
    AlgoCrafterExampleTheme {
        LegalScreenContent(
            state = LegalScreenContentState(
                tosAccepted = true,
                privacyAccepted = true,
                aiDisclaimerAccepted = false,
                allAccepted = false
            ),
            onEvent = {}
        )
    }
}