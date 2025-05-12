package uz.alimov.shapespuzzle.data.module

import org.koin.dsl.module
import uz.alimov.shapespuzzle.data.repository.ResultRepositoryImpl
import uz.alimov.shapespuzzle.domain.repository.ResultRepository

val repositoryModule = module {
    single<ResultRepository> {
        ResultRepositoryImpl(get())
    }
}