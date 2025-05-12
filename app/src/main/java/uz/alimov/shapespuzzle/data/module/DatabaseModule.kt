package uz.alimov.shapespuzzle.data.module

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.alimov.shapespuzzle.data.local.PuzzleDatabase

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = PuzzleDatabase::class.java,
            name = "puzzle_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single {
        get<PuzzleDatabase>().resultsDao()
    }
}