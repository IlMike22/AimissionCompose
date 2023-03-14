package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.*
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
    fun provideGenerateStatisticsUseCase(
        goalRepository: IGoalRepository
    ) = GenerateStatisticsUseCase(
        goalRepository = goalRepository
    )
}