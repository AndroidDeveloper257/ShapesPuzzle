package uz.alimov.shapespuzzle.domain.usecase

import uz.alimov.shapespuzzle.domain.repository.ResultRepository

class GetAllResultsUseCase(
    private val repository: ResultRepository
) {
    fun invoke() = repository.getAllResults()
}