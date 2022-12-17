package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.common.repository.IStatisticsRepository
import com.mind.market.aimissioncompose.data.common.repository.StatisticsRepository
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
        return StatisticsRepository(database.goalDao())
    }
}