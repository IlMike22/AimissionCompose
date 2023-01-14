package com.mind.market.aimissioncompose.statistics.di

import android.app.Application
import androidx.room.Room
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.statistics.data.StatisticsRepository
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {
    @Provides
    @Singleton
    fun provideStatisticsRepository(database: GoalRoomDatabase): IStatisticsRepository {
        return StatisticsRepository(
            localDataSource = database.getStatisticsDao()
        )
    }
}