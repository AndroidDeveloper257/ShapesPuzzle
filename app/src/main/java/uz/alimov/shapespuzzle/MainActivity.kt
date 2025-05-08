package uz.alimov.shapespuzzle

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import uz.alimov.shapespuzzle.model.HoleItem
import uz.alimov.shapespuzzle.model.ShapeItem
import uz.alimov.shapespuzzle.ui.theme.ShapesPuzzleTheme
import uz.alimov.shapespuzzle.utils.Shape
import kotlin.random.Random

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

@Composable
fun ShapesPuzzleApp() {
    val context = LocalContext.current

    var selectedShapeIndex by remember {
        mutableStateOf<Int?>(null)
    }

    val shapes = remember {
        Shape.entries.map { shape ->
            ShapeItem(
                shape = shape,
                color = Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1f
                )
            )
        }.toMutableStateList()
    }

    val holes = remember {
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
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(
                    20.dp
                )
            ) {
                items(shapes.size) { index ->
                    val item = shapes[index]
                    ShapeCard(
                        shape = item.shape,
                        color = if (item.isMatched) Color.Gray else item.color,
                        showX = item.isMatched,
                        onClick = {
                            if (!item.isMatched) selectedShapeIndex = index
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(
                    20.dp
                )
            ) {
                items(holes.size) { index ->
                    val hole = holes[index]
                    HoleCard(
                        shape = hole.shape,
                        color = if (hole.isFilled) shapes.first { it.shape == hole.shape }.color else Color.White,
                        onClick = {
                            val selectedIndex = selectedShapeIndex
                            if (selectedIndex != null) {
                                val selectedShape = shapes[selectedIndex]
                                if (selectedShape.shape == hole.shape) {
                                    shapes[selectedIndex] = selectedShape.copy(isMatched = true)
                                    holes[index] = hole.copy(isFilled = true)
                                    selectedShapeIndex = null
                                } else {
                                    Toast.makeText(context, "Wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
                holes.forEachIndexed { index, hole ->

                }
            }
        }
    }
}

@Composable
fun ShapeCard(
    shape: Shape,
    color: Color,
    showX: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() }
    ) {
        ShapeCanvas(shape, color)
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
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
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