package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.common.repository.CommonStatisticsRepository
import com.mind.market.aimissioncompose.data.common.repository.ICommonStatisticsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonStatisticsModule {
    @Provides
    @Singleton
    fun provideCommonStatisticsRepository(database: GoalRoomDatabase): ICommonStatisticsRepository {
        return CommonStatisticsRepository(database.goalDao())
    }
}