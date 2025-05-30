package uz.alimov.shapespuzzle.presentation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import uz.alimov.shapespuzzle.R
import uz.alimov.shapespuzzle.domain.model.FruitInstance
import uz.alimov.shapespuzzle.domain.model.FruitType

@Composable
fun DraggableFruit(
    fruitInstance: FruitInstance,
    binBounds: List<Rect>,
    chosenFruits: List<FruitType>,
    onCorrectDrop: () -> Unit,
    onWrongDrop: () -> Unit
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val fruitSizeDp = 48.dp
    val fruitSizePx = with(density) { fruitSizeDp.toPx() }

    val originalOffsetPx = remember {
        IntOffset(
            with(density) { fruitInstance.offsetX.toPx().toInt() },
            with(density) { fruitInstance.offsetY.toPx().toInt() }
        )
    }

    val offset = remember {
        Animatable<Offset, AnimationVector2D>(
            Offset(originalOffsetPx.x.toFloat(), originalOffsetPx.y.toFloat()),
            Offset.VectorConverter
        )
    }

    var dragging by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(fruitSizeDp)
            .offset {
                IntOffset(offset.value.x.toInt(), offset.value.y.toInt())
            }
            .zIndex(if (dragging) 1f else 0f)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragging = true
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offset.snapTo(offset.value + dragAmount)
                        }
                    },
                    onDragEnd = {
                        dragging = false

                        val fruitRect = Rect(
                            offset.value.x,
                            offset.value.y,
                            offset.value.x + fruitSizePx,
                            offset.value.y + fruitSizePx
                        )

                        val hitBinIndex = binBounds.indexOfFirst { binRect ->
                            binRect.overlaps(fruitRect)
                        }

                        if (hitBinIndex == -1) {
                            // Not dropped on any bin → do nothing
                        } else {
                            val correctFruit = chosenFruits[hitBinIndex]
                            if (correctFruit == fruitInstance.type) {
                                onCorrectDrop()
                            } else {
                                onWrongDrop()
                                coroutineScope.launch {
                                    offset.animateTo(
                                        Offset(originalOffsetPx.x.toFloat(), originalOffsetPx.y.toFloat()),
                                        animationSpec = tween(300)
                                    )
                                }
                            }
                        }
                    },
                    onDragCancel = {
                        dragging = false
                        coroutineScope.launch {
                            offset.animateTo(
                                Offset(originalOffsetPx.x.toFloat(), originalOffsetPx.y.toFloat()),
                                animationSpec = tween(300)
                            )
                        }
                    }
                )
            }
    ) {
        Icon(
            painter = painterResource(id = fruitInstance.type.iconRes),
            contentDescription = fruitInstance.type.name,
            tint = Color.Unspecified,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun FruitTrashBin(
    fruitType: FruitType,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(80.dp)) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_trash_bin),
            contentDescription = "Trash bin",
            tint = Color.Unspecified
        )

        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopEnd)
                .offset(x = 4.dp, y = (-4).dp),
            painter = painterResource(id = fruitType.iconRes),
            contentDescription = fruitType.name,
            tint = Color.Unspecified
        )
    }
}