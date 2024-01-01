package air.texnodev.puzzle15.room

import air.texnodev.puzzle15.room.dao.ContinueDao
import air.texnodev.puzzle15.room.dao.StatisticsDao
import air.texnodev.puzzle15.room.entity.ContinueModel
import air.texnodev.puzzle15.room.entity.StatisticsModel
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [StatisticsModel::class, ContinueModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    //abstract fun registrationDao(): RegistrationDao
    abstract fun statisticDao():StatisticsDao
    abstract fun continueDao():ContinueDao



    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "DB_NAME"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}