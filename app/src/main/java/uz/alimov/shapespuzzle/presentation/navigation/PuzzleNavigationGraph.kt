package uz.alimov.shapespuzzle.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uz.alimov.shapespuzzle.presentation.screen.history.HistoryScreen
import uz.alimov.shapespuzzle.presentation.screen.home.HomeScreen
import uz.alimov.shapespuzzle.presentation.screen.play.PlayScreen

@Composable
fun PuzzleNavigationGraph(
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateToPlay = { navController.navigate(Play) },
                onNavigateToHistory = { navController.navigate(History) }
            )
        }
        composable<Play> {
            PlayScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable<History> {
            HistoryScreen(
                modifier = Modifier.fillMaxSize(),
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}