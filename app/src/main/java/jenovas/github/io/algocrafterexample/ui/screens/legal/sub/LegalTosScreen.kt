package jenovas.github.io.algocrafterexample.ui.screens.legal.sub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import jenovas.github.io.algocrafterexample.ui.components.SharedScaffold
import jenovas.github.io.algocrafterexample.ui.navigation.LegalScreens
import jenovas.github.io.algocrafterexample.ui.navigation.ObserveAsEvents
import jenovas.github.io.algocrafterexample.ui.navigation.safeNavigateUp
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalHeading
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalScreenContentState
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalScreenEffect
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalScreenEvent
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalSection
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TermsOfServiceScreen(
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

    TermsOfServiceScreenContent(
        state = state.contentState,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermsOfServiceScreenContent(
    state: LegalScreenContentState,
    onEvent: (LegalScreenEvent) -> Unit,
) {
    val scrollState = rememberScrollState()
    SharedScaffold(
        topBarTitle = "Terms of Service",
        topBarImageVector = Lucide.ArrowLeft,
        topBarOnEvent = { onEvent(LegalScreenEvent.NavigateBack) },
        bottomBarTitle = "Accept and Continue",
        bottomBarEnabled = true,
        bottomBarOnEvent = {
            onEvent(LegalScreenEvent.SetTosAccepted(true))
            onEvent(LegalScreenEvent.NavigateBack)
        }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            TermsOfServiceScreenLegalSection()
        }
    }
}

@Composable
private fun TermsOfServiceScreenLegalSection() {
    LegalHeading("Terms of Use")

    Text(
        text = "Welcome to AlgoCrafter (the \"App\"). By using this App, you agree to these Terms of Use and Legal Disclaimer. Please read them carefully before accessing the features of the App. If you do not agree to these terms, you must discontinue using the App.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(24.dp))

    LegalSection(
        title = "Purpose and Scope of the App",
        content = "This App is designed to assist users in creating and testing trading strategies using market data, backtesting tools, and AI analysis. The App is provided solely for research and educational purposes. It does not facilitate live trading, and no actual trades can be executed through the App.",
        number = 1
    )

    LegalSection(
        title = "Not Financial Advice",
        content = "The content and analysis provided in this App, including the results from backtesting, AI analysis, and any other feature, do not constitute financial advice. The creators of this App are not financial advisors, and the information provided should not be interpreted as professional investment guidance. You should always seek personalized financial advice from a qualified professional before making any trading or financial decisions.",
        number = 2
    )

    LegalSection(
        title = "AI Analyzer Disclaimer",
        content = "The AI Analyzer is an automated tool that provides explanations of trading strategies, indicator meanings, suggested improvements, and grades based on performance metrics such as risk, win/loss ratio, trading frequency, and time in the market. However, the AI Analyzer is not a licensed financial advisor and may make errors or provide incomplete or inaccurate information. You should not rely solely on the AI Analyzer's suggestions and must verify any changes or recommendations with your own research and testing.",
        number = 3
    )

    LegalSection(
        title = "Accuracy of Data",
        content = "While we strive to provide accurate and up-to-date data and information, the App is provided \"as is\" and \"as available,\" without any warranties or guarantees regarding the accuracy, reliability, completeness, or timeliness of the data, analysis, or results generated. We disclaim any responsibility for errors, inaccuracies, or omissions in the data provided.",
        number = 4
    )

    LegalSection(
        title = "User Responsibility",
        content = "You are solely responsible for any trading decisions, financial actions, or strategy modifications that you make based on the information provided by this App. The creators and developers of the App are not liable for any financial losses, damages, or consequences resulting from your use of the App, including decisions based on AI analysis or backtesting results.",
        number = 5
    )

    LegalSection(
        title = "Trading Risks",
        content = "Trading in financial markets carries inherent risks, including the risk of substantial financial loss. The App is intended to assist with testing strategies, but it cannot predict future market performance, and past performance of a strategy does not guarantee future results. Users should be aware of the risks involved in trading and should only trade with capital they can afford to lose.",
        number = 6
    )

    LegalSection(
        title = "No Guarantees on Results",
        content = "We do not guarantee any particular outcomes, profits, or performance from using the App's features, including backtesting and AI analysis. The results generated by the App are for research and educational purposes only and are not indicative of future trading performance.",
        number = 7
    )

    LegalSection(
        title = "Limitation of Liability",
        content = "To the fullest extent permitted by law, we disclaim all liability for any direct, indirect, incidental, consequential, or special damages arising from your use of the App or reliance on the information provided, even if we have been advised of the possibility of such damages. This includes, but is not limited to, financial losses, loss of data, or loss of profits.",
        number = 8
    )

    LegalSection(
        title = "Intellectual Property Rights",
        content = "All content, features, code, and data within the App are protected by intellectual property laws. Users are not permitted to copy, distribute, modify, or use any proprietary information from the App without prior written consent from the App creators. Any unauthorized use of the App's content may result in legal action.",
        number = 9
    )

    LegalSection(
        title = "User Accountability for Legal and Tax Compliance",
        content = "You are solely responsible for ensuring that your use of the App complies with applicable laws and regulations in your jurisdiction, including but not limited to financial trading rules, tax obligations, and reporting requirements. The creators of the App are not responsible for any non-compliance with such laws.",
        number = 10
    )

    LegalSection(
        title = "Changes to the Terms",
        content = "We reserve the right to modify or update these Terms of Use and Legal Disclaimer at any time. It is your responsibility to periodically review these terms to stay informed of any changes. Your continued use of the App after changes are posted constitutes your acceptance of the revised terms.",
        number = 11
    )

    LegalSection(
        title = "Termination of Use",
        content = "We reserve the right to terminate or suspend your access to the App, without notice, for violating these terms, misusing the App, or for any other reason at our sole discretion. You agree that we are not liable for any damages or losses arising from the termination of your use of the App.",
        number = 12
    )

    LegalSection(
        title = "Jurisdiction and Governing Law",
        content = "These Terms of Use and Legal Disclaimer are governed by the laws of the United States, without regard to its conflict of law provisions. Any legal disputes or claims arising from your use of the App shall be subject to the exclusive jurisdiction of the courts located in the United States.",
        number = 13
    )

    LegalSection(
        title = "Indemnification",
        content = "You agree to indemnify, defend, and hold harmless AlgoCrafter, its officers, directors, employees, and agents from any claims, damages, liabilities, costs, and expenses (including legal fees) arising from your use of the App, your violation of these terms, or any activity you engage in through the App.",
        number = 14
    )

    LegalSection(
        title = "Consent to Data Collection",
        content = "By using this App, you consent to our collection, storage, and use of your data as outlined in our Privacy Policy. We may collect data related to your use of the App to improve our services, but we do not sell or share your personal information with third parties without your consent, unless required by law. Please review our Privacy Policy for further details on how your data is handled.",
        number = 15
    )

    LegalSection(
        title = "Contact Information",
        content = "If you have any questions about these Terms of Use and Legal Disclaimer, please contact us at support@algocrafter.app",
        number = 16
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "By using this App, you acknowledge that you have read, understood, and agreed to these Terms of Use and Legal Disclaimer. If you do not agree, you must discontinue using the App immediately.",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(24.dp))
}