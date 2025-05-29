package uz.alimov.shapespuzzle.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uz.alimov.shapespuzzle.presentation.screen.fruit_basket.FruitBasketScreen
import uz.alimov.shapespuzzle.presentation.screen.history.HistoryScreen
import uz.alimov.shapespuzzle.presentation.screen.home.HomeScreen
import uz.alimov.shapespuzzle.presentation.screen.play.PlayScreen
import uz.alimov.shapespuzzle.presentation.screen.testing.FruitSortingGame

@Composable
fun PuzzleNavigationGraph(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        NavHost(
            modifier = Modifier.fillMaxSize().padding(padding),
            navController = navController,
            startDestination = Home
        ) {
            composable<Home> {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onNavigateToPlayPuzzle = { navController.navigate(PlayPuzzle) },
                    onNavigateToSorting = { navController.navigate(Sorting) },
                    onNavigateToHistory = { navController.navigate(History) }
                )
            }
            composable<PlayPuzzle> {
                PlayScreen(
                    modifier = Modifier.fillMaxSize(),
                    onNavigateBack = { navController.navigateUp() }
                )
            }
            composable<Sorting> {
                FruitSortingGame()
            }
            composable<History> {
                HistoryScreen(
                    modifier = Modifier.fillMaxSize(),
                    navigateUp = { navController.navigateUp() }
                )
            }
        }

    }
}