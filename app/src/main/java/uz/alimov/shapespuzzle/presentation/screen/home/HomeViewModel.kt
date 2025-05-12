package uz.alimov.shapespuzzle.presentation.screen.home

import androidx.lifecycle.ViewModel
import uz.alimov.shapespuzzle.domain.usecase.GetAllResultsUseCase

class HomeViewModel(
    private val getAllResultsUseCase: GetAllResultsUseCase
) : ViewModel() {
}