package uz.alimov.shapespuzzle.domain.model

import uz.alimov.shapespuzzle.utils.Shape

data class HoleItem(
    val shape: Shape,
    var isFilled: Boolean = false
)