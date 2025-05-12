package uz.alimov.shapespuzzle.domain.model

data class Result(
    val id: Int = 0,
    val time: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)