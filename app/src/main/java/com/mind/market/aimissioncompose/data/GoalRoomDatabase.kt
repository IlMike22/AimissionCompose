package com.mind.market.aimissioncompose.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mind.market.aimissioncompose.data.dto.GoalDto
import com.mind.market.aimissioncompose.statistics.data.IStatisticsEntityDao
import com.mind.market.aimissioncompose.statistics.data.dto.StatisticsEntityDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [GoalDto::class, StatisticsEntityDto::class],
    version = 4,
    autoMigrations = [AutoMigration(from = 2, to = 3)]
)
@TypeConverters(Converters::class)
abstract class GoalRoomDatabase : RoomDatabase() {
    abstract fun goalDao(): IGoalDao
    abstract fun getStatisticsDao(): IStatisticsEntityDao

    private class GoalDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.goalDao(),
                        database.getStatisticsDao()
                    )
                }
            }
        }

        suspend fun populateDatabase(goalDao: IGoalDao, statisticsDao:IStatisticsEntityDao) {
            goalDao.deleteAll()
            statisticsDao.deleteAll()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GoalRoomDatabase? = null

        fun getDatabase(
            context: Context, scope: CoroutineScope
        ): GoalRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalRoomDatabase::class.java,
                    "goal_database"
                ).addCallback(GoalDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}