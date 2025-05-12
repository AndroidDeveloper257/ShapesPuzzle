package uz.alimov.shapespuzzle.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.alimov.shapespuzzle.domain.model.Result
import uz.alimov.shapespuzzle.domain.repository.ResultRepository

class GetRecordUseCase(
    private val repository: ResultRepository
) {
    fun invoke(): Flow<Result?> {
        return repository.getResultWithMinTime()
    }
}