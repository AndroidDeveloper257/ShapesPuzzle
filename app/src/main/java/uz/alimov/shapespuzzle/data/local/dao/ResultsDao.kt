package uz.alimov.shapespuzzle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.alimov.shapespuzzle.data.local.entity.ResultEntity

@Dao
interface ResultsDao {

    @Insert(entity = ResultEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ResultEntity)

    @Query("SELECT * FROM results_table ORDER BY time ASC")
    fun getAllResults(): Flow<List<ResultEntity>>

    @Query("SELECT * FROM results_table ORDER BY time ASC LIMIT 1")
    fun getResultWithMinTime(): Flow<ResultEntity?>

}