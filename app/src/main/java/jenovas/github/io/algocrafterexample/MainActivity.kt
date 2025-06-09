package jenovas.github.io.algocrafterexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import jenovas.github.io.algocrafterexample.ui.navigation.AppNavigation
import jenovas.github.io.algocrafterexample.ui.theme.AlgoCrafterExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlgoCrafterExampleTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                )
            }
        }
    }
}