package com.mind.market.aimissioncompose.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mind.market.aimissioncompose.auth.data.IAuthenticationDao
import com.mind.market.aimissioncompose.auth.data.model.UserDto
import com.mind.market.aimissioncompose.data.dto.GoalDto
import com.mind.market.aimissioncompose.stocks_diary.overview.data.IStocksDiaryDao
import com.mind.market.aimissioncompose.stocks_diary.overview.data.StocksDiaryDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [GoalDto::class, UserDto::class, StocksDiaryDto::class],
    version = 7,
    exportSchema = false
//    autoMigrations = [AutoMigration(from = 3, to = 4), AutoMigration(from = 5, to = 6)]
)
@TypeConverters(Converters::class)
abstract class GoalRoomDatabase : RoomDatabase() {
    abstract fun goalDao(): IGoalDao

    abstract fun authenticationDao(): IAuthenticationDao

    abstract fun stocksDiaryDao(): IStocksDiaryDao

    private class GoalDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.goalDao()
                    )
                }
            }
        }

        suspend fun populateDatabase(goalDao: IGoalDao) {
            goalDao.deleteAll()
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