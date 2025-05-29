package uz.alimov.shapespuzzle.domain.model

import androidx.compose.ui.unit.Dp

data class FruitInstance(
    val id: Int,
    val type: FruitType,
    val offsetX: Dp,
    val offsetY: Dp
)