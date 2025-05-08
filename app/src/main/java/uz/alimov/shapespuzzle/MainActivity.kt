package uz.alimov.shapespuzzle

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import uz.alimov.shapespuzzle.model.HoleItem
import uz.alimov.shapespuzzle.model.ShapeItem
import uz.alimov.shapespuzzle.ui.theme.ShapesPuzzleTheme
import uz.alimov.shapespuzzle.utils.Shape
import uz.alimov.shapespuzzle.utils.shapeColors
import kotlin.coroutines.cancellation.CancellationException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShapesPuzzleTheme {
                ShapesPuzzleApp()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShapesPuzzleApp() {
    val context = LocalContext.current

    var selectedShapeIndex by remember {
        mutableStateOf<Int?>(null)
    }

    val shuffledColors = shapeColors.shuffled()

    val shapes = remember {
        Shape.entries.mapIndexed { index, shape ->
            ShapeItem(
                shape = shape,
                color = shuffledColors[index]
            )
        }.toMutableStateList()
    }

    var holes = remember {
        Shape.entries.shuffled().map { shape ->
            HoleItem(shape = shape)
        }.toMutableStateList()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                shapes.forEachIndexed { index, item ->
                    ShapeCard(
                        shape = item,
                        color = if (item.isMatched) Color.Gray else item.color,
                        showX = item.isMatched,
                        onSelect = {
                            selectedShapeIndex = index
                            shapes.indices.forEach { i ->
                                shapes[i] = shapes[i].copy(isSelected = i == index)
                            }
                        },
                        onDeselect = {
                            shapes[index] = item.copy(isSelected = false)
                            selectedShapeIndex = null
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                holes.forEachIndexed { index, hole ->
                    HoleCard(
                        shape = hole.shape,
                        color = if (hole.isFilled) shapes.first { it.shape == hole.shape }.color else Color.White,
                        isFilled = hole.isFilled,
                        onClick = {
                            selectedShapeIndex?.let { selectedIndex ->
                                if (selectedIndex != -1) {
                                    val selectedShape = shapes[selectedIndex]
                                    if (selectedShape.shape == hole.shape) {
                                        shapes[selectedIndex] =
                                            selectedShape.copy(isMatched = true, isSelected = false)
                                        holes[index] = hole.copy(isFilled = true)
                                    } else {
                                        Toast.makeText(context, "Wrong!", Toast.LENGTH_SHORT).show()
                                        shapes[selectedIndex] =
                                            selectedShape.copy(isSelected = false)
                                        selectedShapeIndex = null
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun ShapeCard(
    shape: ShapeItem,
    color: Color,
    showX: Boolean,
    onSelect: () -> Unit,
    onDeselect: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }

    // React to isSelected and isMatched changes
    LaunchedEffect(shape.isSelected, shape.isMatched) {
        scale = when {
            shape.isMatched -> 1f // no effect when matched
            shape.isSelected -> 0.9f // selected resting scale
            else -> 1f // unselected resting scale
        }
    }

    val targetScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(100)
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(10.dp)
            .graphicsLayer(scaleX = targetScale, scaleY = targetScale)
            .pointerInput(shape.isMatched, shape.isSelected) {
                if (!shape.isMatched) {
                    detectTapGestures(
                        onPress = {
                            scale = if (shape.isSelected) 0.7f else 0.8f
                            val releasedInside = try {
                                awaitRelease()
                                true
                            } catch (e: CancellationException) {
                                false
                            }

                            if (releasedInside) {
                                if (shape.isSelected) {
                                    onDeselect()
                                } else {
                                    onSelect()
                                }
                            } else {
                                scale = if (shape.isSelected) 0.9f else 1f
                            }
                        }
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        ShapeCanvas(shape.shape, color)
        if (showX) {
            Text(
                text = "X",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun HoleCard(
    shape: Shape,
    color: Color,
    isFilled: Boolean,
    onClick: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(100)
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(10.dp)
            .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
            .then(
                if (!isFilled) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                scale = 0.9f
                                try {
                                    awaitRelease()
                                } finally {
                                    scale = 1f
                                    onClick()
                                }
                            }
                        )
                    }
                } else Modifier // no gesture when filled
            ),
        contentAlignment = Alignment.Center
    ) {
        ShapeCanvas(shape, color)
    }
}

@Composable
fun ShapeCanvas(shape: Shape, color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        when (shape) {
            Shape.CIRCLE -> drawCircle(color)
            Shape.RECTANGLE -> drawRect(color)
            else -> {
                val vertices = when (shape) {
                    Shape.TRIANGLE -> 3
                    Shape.PENTAGON -> 5
                    Shape.HEXAGON -> 6
                    else -> 0
                }
                if (vertices > 0) {
                    val polygon = RoundedPolygon(
                        numVertices = vertices,
                        radius = size.minDimension / 2,
                        centerX = size.width / 2,
                        centerY = size.height / 2
                    )
                    val path = polygon.toPath().asComposePath()
                    drawPath(path, color = color)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShapesPuzzleTheme {
        ShapesPuzzleApp()
    }
}