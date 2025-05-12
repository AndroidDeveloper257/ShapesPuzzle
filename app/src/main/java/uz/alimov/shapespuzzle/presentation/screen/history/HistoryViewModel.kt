package uz.alimov.shapespuzzle.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.alimov.shapespuzzle.domain.model.Result
import uz.alimov.shapespuzzle.domain.usecase.GetAllResultsUseCase
import uz.alimov.shapespuzzle.domain.usecase.GetRecordUseCase

class HistoryViewModel(
    private val getAllResultsUseCase: GetAllResultsUseCase,
    private val getRecordUseCase: GetRecordUseCase
) : ViewModel() {

    private val _highestScore = MutableStateFlow(Result())
    val highestScore get() = _highestScore

    private val _results = MutableStateFlow<List<Result>>(emptyList())
    val results get() = _results

    init {
        getAllResults()
        getRecord()
    }

    private fun getAllResults() {
        viewModelScope.launch {
            getAllResultsUseCase.invoke().collect {
                _results.value = it
            }
        }
    }

    private fun getRecord() {
        viewModelScope.launch {
            getRecordUseCase.invoke().collect {
                _highestScore.value = it ?: Result()
            }
        }
    }

}