package uz.alimov.shapespuzzle.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.alimov.shapespuzzle.domain.model.Result

interface ResultRepository {

    suspend fun insertResult(result: Result)

    fun getAllResults(): Flow<List<Result>>

    fun getResultWithMinTime(): Flow<Result?>

}