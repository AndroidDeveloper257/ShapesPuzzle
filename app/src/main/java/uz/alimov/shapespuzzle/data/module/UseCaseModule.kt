package uz.alimov.shapespuzzle.data.module

import org.koin.dsl.module
import uz.alimov.shapespuzzle.domain.usecase.AddResultUseCase
import uz.alimov.shapespuzzle.domain.usecase.GetAllResultsUseCase
import uz.alimov.shapespuzzle.domain.usecase.GetRecordUseCase

val useCaseModule = module {
    single {
        AddResultUseCase(get())
    }
    single {
        GetAllResultsUseCase(get())
    }
    single {
        GetRecordUseCase(get())
    }
}