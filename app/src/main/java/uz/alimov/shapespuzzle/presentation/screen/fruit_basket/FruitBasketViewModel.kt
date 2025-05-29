package uz.alimov.shapespuzzle.presentation.screen.fruit_basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.alimov.shapespuzzle.domain.model.Result
import uz.alimov.shapespuzzle.domain.usecase.AddResultUseCase
import uz.alimov.shapespuzzle.domain.usecase.GetRecordUseCase

class FruitBasketViewModel(
    private val addResultUseCase: AddResultUseCase,
    private val getRecordUseCase: GetRecordUseCase
) : ViewModel() {

    private val _recordState = MutableStateFlow(Result())
    val recordState get() = _recordState.asStateFlow()

    init {
        getRecord()
    }

    private fun getRecord() {
        viewModelScope.launch {
            getRecordUseCase.invoke()
                .collect {
                    _recordState.value = it ?: Result()
                }
        }
    }

    fun addResult(result: Result) {
        viewModelScope.launch {
            addResultUseCase.invoke(result)
        }
    }

}