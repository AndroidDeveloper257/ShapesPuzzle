package uz.alimov.shapespuzzle.domain.model

import androidx.compose.ui.geometry.Offset

data class FruitItem(
    val id: Int,
    val type: FruitType,
    var position: Offset,
    var originalPosition: Offset = position,
    var isDragging: Boolean = false,
    var isVisible: Boolean = true
)