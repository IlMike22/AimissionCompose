package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.landing_page.use_case.ILandingPageUseCase
import com.mind.market.aimissioncompose.domain.landing_page.use_case.implementation.LandingPageUseCase
import com.mind.market.aimissioncompose.statistics.data.StatisticsRepository
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LandingPageModule {
    @Provides
    @Singleton
    fun provideUseCase(
        goalRepository: IGoalRepository,
        statisticsRepository: IStatisticsRepository
    ): ILandingPageUseCase {
        return LandingPageUseCase(
            goalRepository = goalRepository,
            statisticsRepository = statisticsRepository
        )
    }
}