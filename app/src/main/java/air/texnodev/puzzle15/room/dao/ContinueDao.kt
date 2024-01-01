package air.texnodev.puzzle15.room.dao

import air.texnodev.puzzle15.room.entity.ContinueModel
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContinueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContinue(continueModel: ContinueModel):Long

    @Query("SELECT * FROM ContinueModel")
    fun getContinue():LiveData<ContinueModel>

    @Query("DELETE FROM ContinueModel")
    fun clear()

}