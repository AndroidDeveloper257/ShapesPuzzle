package uz.alimov.shapespuzzle.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.alimov.shapespuzzle.presentation.ui.theme.ShapesPuzzleTheme
import uz.alimov.shapespuzzle.utils.GameMode

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToPlayPuzzle: () -> Unit,
    onNavigateToSorting: (GameMode) -> Unit,
    onNavigateToHistory: () -> Unit
) {
    var showModeDialog by remember {
        mutableStateOf(false)
    }
    var showExitDialog by remember {
        mutableStateOf(false)
    }

    if (showModeDialog) {
        ChooseGameMode(
            onModeSelected = {
                onNavigateToSorting(it)
                showModeDialog = false
            },
            onDismiss = {
                showModeDialog = false
            }
        )
    }

    if (showExitDialog) {
        ExitDialog(
            onConfirm = {
                showExitDialog = false
            },
            onCancel = {
                showExitDialog = false
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 10.dp),
//            onClick = onNavigateToPlayPuzzle
//        ) {
//            Text(text = "Play shapes puzzle")
//        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            onClick = {
                showModeDialog = true
            }
        ) {
            Text(text = "Play fruit sorting")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onClick = {
                showExitDialog = true
            }
        ) {
            Text(text = "Exit")
        }
    }
}

@Composable
fun ChooseGameMode(
    onModeSelected: (GameMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Choose game mode")
        },
        text = {
            Column {
                GameMode.entries.forEach { gameMode ->
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        onClick = {
                            onModeSelected(gameMode)
                            onDismiss()
                        }
                    ) {
                        Text(text = gameMode.name)
                    }
                }
            }
        },
        confirmButton = {

        }
    )
}

@Composable
fun ExitDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Exit")
        },
        text = {
            Text(text = "Are you sure you want to exit?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(text = "Cancel")
            }
        }
    )
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