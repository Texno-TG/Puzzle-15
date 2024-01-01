package air.texnodev.puzzle15.room.dao

import air.texnodev.puzzle15.room.entity.StatisticsModel
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StatisticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(statisticsModel: StatisticsModel):Long

    @Query("SELECT * FROM StatisticsModel ORDER BY step, time ASC LIMIT 10")
    fun getTopGame(): LiveData<List<StatisticsModel>>
}