package uz.alimov.shapespuzzle.utils

enum class GameMode(
    val fruitTypes: Int,
    val fruitAmount: Int
) {
    EASY(3, 4),
    MEDIUM(4, 5),
    HARD(5, 6)
}