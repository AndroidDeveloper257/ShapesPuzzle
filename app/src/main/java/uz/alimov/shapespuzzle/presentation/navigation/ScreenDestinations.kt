package uz.alimov.shapespuzzle.presentation.navigation

import kotlinx.serialization.Serializable
import uz.alimov.shapespuzzle.utils.GameMode

@Serializable
object Home

@Serializable
object PlayPuzzle

@Serializable
data class Sorting(
    val mode: GameMode
)

@Serializable
object History