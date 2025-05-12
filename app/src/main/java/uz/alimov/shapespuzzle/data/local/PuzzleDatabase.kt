package uz.alimov.shapespuzzle.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.alimov.shapespuzzle.data.local.dao.ResultsDao
import uz.alimov.shapespuzzle.data.local.entity.ResultEntity

@Database(
    entities = [
        ResultEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PuzzleDatabase(): RoomDatabase() {
    abstract fun resultsDao(): ResultsDao
}