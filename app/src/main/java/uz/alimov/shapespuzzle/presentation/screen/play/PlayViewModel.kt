package uz.alimov.shapespuzzle.presentation.screen.play

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.alimov.shapespuzzle.domain.model.Result
import uz.alimov.shapespuzzle.domain.usecase.AddResultUseCase
import uz.alimov.shapespuzzle.domain.usecase.GetRecordUseCase

class PlayViewModel(
    private val addResultUseCase: AddResultUseCase,
    private val getRecordUseCase: GetRecordUseCase
) : ViewModel() {

    private val TAG = "PlayViewModel"

    private val _recordState = MutableStateFlow(Result())
    val recordState get() = _recordState.asStateFlow()

    init {
        Log.d(TAG, "initializing view model")
        getRecord()
    }

    private fun getRecord() {
        Log.d(TAG, "getRecord: outside the viewModelScope.launch")
        viewModelScope.launch {
            Log.d(TAG, "getRecord: inside the viewModelScope.launch")
            Log.d(TAG, "getRecord: calling getRecordUseCase.invoke()")
            getRecordUseCase.invoke()
                .onStart {
                    Log.d(TAG, "getRecord is loading")
                }
                .catch {
                    Log.e(TAG, "getRecord: ${it.message}")
                }
                .collect {
                    Log.d(TAG, "getRecord: from collect block $it")
//                    _recordState.update { it.copy(time = it.time) }
                    _recordState.value = it ?: Result()
                    Log.d(TAG, "getRecord: ${_recordState.value}")
                }
        }
    }

    fun addResult(result: Result) {
        viewModelScope.launch {
            addResultUseCase.invoke(result)
        }
    }

}