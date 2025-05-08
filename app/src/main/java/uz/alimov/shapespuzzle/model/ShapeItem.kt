package uz.alimov.shapespuzzle.model

import androidx.compose.ui.graphics.Color
import uz.alimov.shapespuzzle.utils.Shape

data class ShapeItem(
    val shape: Shape,
    val color: Color,
    val isMatched: Boolean = false,
    val isSelected: Boolean = false
)