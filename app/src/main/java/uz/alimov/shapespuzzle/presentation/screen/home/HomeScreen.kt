package uz.alimov.shapespuzzle.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.alimov.shapespuzzle.presentation.ui.theme.ShapesPuzzleTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToPlayPuzzle: () -> Unit,
    onNavigateToSorting: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            onClick = onNavigateToPlayPuzzle
        ) {
            Text(text = "Play shapes puzzle")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            onClick = onNavigateToSorting
        ) {
            Text(text = "Play fruit sorting")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onClick = onNavigateToHistory
        ) {
            Text(text = "History")
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    ShapesPuzzleTheme {
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            onNavigateToPlayPuzzle = {},
            onNavigateToSorting = {},
            onNavigateToHistory = {}
        )
    }
}