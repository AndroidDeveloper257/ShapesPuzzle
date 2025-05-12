package uz.alimov.shapespuzzle.domain.usecase

import uz.alimov.shapespuzzle.domain.model.Result
import uz.alimov.shapespuzzle.domain.repository.ResultRepository

class AddResultUseCase(
    private val repository: ResultRepository
) {
    suspend fun invoke(result: Result) {
        repository.insertResult(result)
    }
}