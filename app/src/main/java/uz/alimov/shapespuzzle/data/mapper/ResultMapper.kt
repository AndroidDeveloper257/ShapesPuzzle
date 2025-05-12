package uz.alimov.shapespuzzle.data.mapper

import uz.alimov.shapespuzzle.data.local.entity.ResultEntity
import uz.alimov.shapespuzzle.domain.model.Result

fun ResultEntity.toResult(): Result {
    return Result(
        id = id,
        time = time,
        createdAt = createdAt
    )
}

fun Result.toResultEntity(): ResultEntity {
    return ResultEntity(
        id = id,
        time = time,
        createdAt = createdAt
    )
}