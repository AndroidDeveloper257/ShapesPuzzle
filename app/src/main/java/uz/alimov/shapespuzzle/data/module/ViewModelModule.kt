package uz.alimov.shapespuzzle.data.module

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uz.alimov.shapespuzzle.presentation.screen.history.HistoryViewModel
import uz.alimov.shapespuzzle.presentation.screen.play.PlayViewModel

val viewModelModule = module {
    viewModel {
        PlayViewModel(
            addResultUseCase = get(),
            getRecordUseCase = get()
        )
    }
    viewModel {
        HistoryViewModel(
            getAllResultsUseCase = get(),
            getRecordUseCase = get()
        )
    }
}