package uz.alimov.shapespuzzle.presentation.screen.fruit_basket

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.alimov.shapespuzzle.R
import uz.alimov.shapespuzzle.domain.model.FruitInstance
import uz.alimov.shapespuzzle.domain.model.FruitType
import uz.alimov.shapespuzzle.presentation.component.Congratulate
import uz.alimov.shapespuzzle.presentation.component.DisplayLottieAnimation
import uz.alimov.shapespuzzle.presentation.component.DraggableFruit
import uz.alimov.shapespuzzle.presentation.component.FruitTrashBin
import uz.alimov.shapespuzzle.utils.GameMode

@Composable
fun FruitBasketScreen(
    mode: GameMode,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val successSoundPlayer = remember {
        MediaPlayer.create(context, R.raw.success_sound)
    }
    val errorSoundPlayer = remember {
        MediaPlayer.create(context, R.raw.error_sound)
    }

    var currentLottieRes by remember { mutableStateOf<Int?>(null) }
    var isGameOver by remember { mutableStateOf(false) }

    val allFruits = FruitType.entries.toList()
    val chosenFruits = remember { allFruits.shuffled().take(mode.fruitTypes) }

    val binBounds = remember { mutableStateListOf<Rect>() }

    val initialFruits = remember {
        val list = mutableListOf<FruitInstance>()
        var id = 0
        repeat(mode.fruitAmount) {
            chosenFruits.forEach { fruit ->
                val x = (100..300).random().toFloat()
                val y = (100..600).random().toFloat()
                list.add(FruitInstance(id++, fruit, x.dp, y.dp))
            }
        }
        list
    }

    val fruits = remember { mutableStateListOf<FruitInstance>().apply { addAll(initialFruits) } }

    if (isGameOver) {
        Congratulate {
            isGameOver = false
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            val allPositions = listOf(
                Alignment.TopStart,
                Alignment.TopEnd,
                Alignment.BottomStart,
                Alignment.BottomEnd,
                Alignment.CenterStart,
                Alignment.CenterEnd
            )

            val binPositions = remember(mode) {
                allPositions.shuffled().take(mode.fruitTypes)
            }

            binBounds.clear()
            binPositions.forEachIndexed { index, alignment ->
                Box(
                    modifier = Modifier
                        .align(alignment)
                        .padding(16.dp)
                        .onGloballyPositioned { coordinates ->
                            val rect = coordinates.boundsInWindow()
                            if (binBounds.size > index) {
                                binBounds[index] = rect
                            } else {
                                binBounds.add(rect)
                            }
                        }
                ) {
                    FruitTrashBin(fruitType = chosenFruits[index])
                }
            }

            fruits.forEach { fruitInstance ->
                key(fruitInstance.id) {
                    DraggableFruit(
                        fruitInstance = fruitInstance,
                        binBounds = binBounds,
                        chosenFruits = chosenFruits,
                        onCorrectDrop = {
                            fruits.remove(fruitInstance)
                            if (fruits.isEmpty()) {
                                isGameOver = true
                            } else {
                                currentLottieRes = R.raw.correct
                                successSoundPlayer.start()
                            }
                        },
                        onWrongDrop = {
                            errorSoundPlayer.start()
                            currentLottieRes = R.raw.wrong
                        }
                    )
                }
            }

            currentLottieRes?.let { resId ->
                DisplayLottieAnimation(
                    resId = resId,
                    onDismiss = {
                        currentLottieRes = null
                    }
                )
            }
        }

    }

}

@Preview
@Composable
private fun FruitBasketScreenPreview() {
    FruitBasketScreen(
        mode = GameMode.EASY,
        onNavigateBack = {}
    )
}