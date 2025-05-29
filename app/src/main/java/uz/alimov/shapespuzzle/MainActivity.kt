package uz.alimov.shapespuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinContext
import uz.alimov.shapespuzzle.presentation.navigation.PuzzleNavigationGraph
import uz.alimov.shapespuzzle.presentation.ui.theme.ShapesPuzzleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinContext {
                val navController = rememberNavController()
                PuzzleNavigationGraph(
                    navController = navController,
                    onExitApp = {
                        finish()
                    })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShapesPuzzleTheme {
        PuzzleNavigationGraph(
            navController = rememberNavController(),
            onExitApp = {}
        )
    }
}