package jenovas.github.io.algocrafterexample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import jenovas.github.io.algocrafterexample.ui.screens.legal.LegalScreen
import jenovas.github.io.algocrafterexample.ui.screens.legal.sub.AIDisclaimerScreen
import jenovas.github.io.algocrafterexample.ui.screens.legal.sub.PrivacyPolicyScreen
import jenovas.github.io.algocrafterexample.ui.screens.legal.sub.TermsOfServiceScreen
import jenovas.github.io.algocrafterexample.ui.screens.menu.MenuScreen
import jenovas.github.io.algocrafterexample.ui.screens.strategies.StrategiesScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: Any = LegalScreens,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Menu> {
            MenuScreen(navController)
        }

        composable<Strategies> {
            StrategiesScreen(navController)
        }

        navigation<LegalScreens>(startDestination = Legal) {
            composable<Legal> {
                LegalScreen(navController)
            }

            composable<PrivacyPolicy> {
                PrivacyPolicyScreen(navController)
            }

            composable<TermsOfService> {
                TermsOfServiceScreen(navController)
            }

            composable<AIDisclaimer> {
                AIDisclaimerScreen(navController)
            }
        }
    }
} 