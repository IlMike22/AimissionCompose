package com.mind.market.aimissioncompose.di

import android.app.Application
import androidx.room.Room
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.common.repository.GoalRepository
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoalModule {
    @Provides
    @Singleton
    fun provideGoalRepository(database: GoalRoomDatabase): IGoalRepository {
        return GoalRepository(
            goalDao = database.goalDao()
        )
    }

    @Provides
    @Singleton
    fun provideGoalDatabase(app: Application): GoalRoomDatabase {
        return Room.databaseBuilder(
            app,
            GoalRoomDatabase::class.java,
            "goal_database"
        ).build()
    }
}