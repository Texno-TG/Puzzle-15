package air.texnodev.puzzle15.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StatisticsModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var step: Int = 0,
    var time: Int = 0,

)
