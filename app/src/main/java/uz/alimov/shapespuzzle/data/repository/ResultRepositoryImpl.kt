package uz.alimov.shapespuzzle.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uz.alimov.shapespuzzle.data.local.dao.ResultsDao
import uz.alimov.shapespuzzle.data.mapper.toResult
import uz.alimov.shapespuzzle.data.mapper.toResultEntity
import uz.alimov.shapespuzzle.domain.model.Result
import uz.alimov.shapespuzzle.domain.repository.ResultRepository

class ResultRepositoryImpl(
    private val resultsDao: ResultsDao
) : ResultRepository {

    override suspend fun insertResult(result: Result) {
        resultsDao.insertResult(result.toResultEntity())
    }

    override fun getAllResults(): Flow<List<Result>> {
        val allResults = resultsDao.getAllResults()
        return flow {
            allResults
                .collect {
                    emit(it.map { it.toResult() })
                }
        }
    }

    override fun getResultWithMinTime(): Flow<Result?> {
        val resultWithMinTime = resultsDao.getResultWithMinTime()
        return flow {
            resultWithMinTime
                .catch {
                    emit(null)
                }
                .collect {
                    emit(it?.toResult())
                }
        }
    }
}