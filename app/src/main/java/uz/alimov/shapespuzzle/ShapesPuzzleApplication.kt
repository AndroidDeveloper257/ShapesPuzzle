package uz.alimov.shapespuzzle

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.alimov.shapespuzzle.data.module.databaseModule
import uz.alimov.shapespuzzle.data.module.repositoryModule
import uz.alimov.shapespuzzle.data.module.useCaseModule
import uz.alimov.shapespuzzle.data.module.viewModelModule

class ShapesPuzzleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ShapesPuzzleApplication)
            modules(
                databaseModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }

}