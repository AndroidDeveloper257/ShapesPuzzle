package uz.alimov.shapespuzzle.presentation.screen.fruit_basket

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import uz.alimov.shapespuzzle.domain.model.FruitItem
import uz.alimov.shapespuzzle.domain.model.FruitType
import kotlin.random.Random

@Composable
fun FruitBasketScreen(modifier: Modifier = Modifier) {
    var screenSize by remember { mutableStateOf(IntSize.Zero) }
    val fruitItems = remember { mutableStateListOf<FruitItem>() }
    val basketPositions = remember { mutableStateMapOf<FruitType, Offset>() }

    val context = LocalContext.current
    val density = LocalDensity.current
    val basketSizePx = with(density) { 64.dp.toPx() }
    val fruitSize = with(density) { 48.dp.toPx() }

    // Selected 4 fruits
    val selectedFruits = remember { FruitType.entries.toList().shuffled().take(4) }

    // Generate fruits only after screen is measured
    LaunchedEffect(screenSize) {
        if (screenSize.width > 0 && screenSize.height > 0 && fruitItems.isEmpty()) {
            fruitItems.addAll(generateRandomFruits(screenSize))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { screenSize = it.size }
    ) {
        // Draw baskets at corners
        selectedFruits.forEachIndexed { index, type ->
            val alignment = when (index) {
                0 -> Alignment.TopStart
                1 -> Alignment.TopEnd
                2 -> Alignment.BottomStart
                3 -> Alignment.BottomEnd
                else -> Alignment.Center
            }

            Box(
                modifier = Modifier
                    .align(alignment)
                    .padding(16.dp)
                    .size(64.dp)
                    .background(Color.LightGray, shape = CircleShape)
                    .onGloballyPositioned { coords ->
                        val pos = coords.positionInRoot()
                        basketPositions[type] = pos
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = type.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Draw fruits
        fruitItems.forEach { fruit ->
            DraggableFruit(
                fruit = fruit,
                fruitSize = fruitSize,
                basketPositions = basketPositions,
                basketSizePx = basketSizePx,
                onCorrectDrop = { fruitToRemove ->
                    fruitItems.remove(fruitToRemove)
                },
                onWrongDrop = {
                    Toast.makeText(context, "Wrong basket!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun DraggableFruit(
    fruit: FruitItem,
    fruitSize: Float,
    basketPositions: Map<FruitType, Offset>,
    basketSizePx: Float,
    onCorrectDrop: (FruitItem) -> Unit,
    onWrongDrop: () -> Unit
) {
    var offset by remember { mutableStateOf(fruit.position) }

    Image(
        painter = painterResource(id = fruit.type.iconRes),
        contentDescription = null,
        modifier = Modifier
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y
            }
            .size(with(LocalDensity.current) { fruitSize.toDp() })
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        fruit.isDragging = true
                    },
                    onDragEnd = {
                        fruit.isDragging = false

                        val dropPos = offset
                        val matchedBin = basketPositions.entries.find { (type, binOffset) ->
                            val inX = dropPos.x + fruitSize / 2 in binOffset.x..(binOffset.x + basketSizePx)
                            val inY = dropPos.y + fruitSize / 2 in binOffset.y..(binOffset.y + basketSizePx)
                            inX && inY
                        }

                        if (matchedBin != null) {
                            if (matchedBin.key == fruit.type) {
                                onCorrectDrop(fruit)
                            } else {
                                offset = fruit.originalPosition
                                onWrongDrop()
                            }
                        } else {
                            offset = fruit.originalPosition
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    }
                )
            }
    )
}

fun generateRandomFruits(screenSize: IntSize): List<FruitItem> {
    val selectedTypes = FruitType.entries.toList().shuffled().take(4)
    val fruitList = mutableListOf<FruitItem>()
    var idCounter = 0
    val safeMargin = 150

    selectedTypes.forEach { type ->
        repeat(6) {
            var x: Float
            var y: Float

            do {
                x = Random.nextInt(0, screenSize.width).toFloat()
                y = Random.nextInt(0, screenSize.height).toFloat()
            } while (
                (x < safeMargin && y < safeMargin) || // Top-left
                (x > screenSize.width - safeMargin && y < safeMargin) || // Top-right
                (x < safeMargin && y > screenSize.height - safeMargin) || // Bottom-left
                (x > screenSize.width - safeMargin && y > screenSize.height - safeMargin) // Bottom-right
            )

            val offset = Offset(x, y)
            fruitList.add(
                FruitItem(
                    id = idCounter++,
                    type = type,
                    position = offset,
                    originalPosition = offset
                )
            )
        }
    }

    return fruitList
}

@Preview
@Composable
private fun FruitBasketScreenPreview() {
    FruitBasketScreen()
}