package com.mind.market.aimissioncompose.di


import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.detail.use_case.IDetailUseCase
import com.mind.market.aimissioncompose.domain.detail.use_case.implementation.DetailUseCase
import com.mind.market.aimissioncompose.domain.detail.use_case.implementation.UpdateStatisticsWithNewGoalCreatedUseCase
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailModule {
    @Provides
    @Singleton
    fun provideUseCase(repository: IGoalRepository): IDetailUseCase {
        return DetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideStatisticsUseCase(repository: IStatisticsRepository): UpdateStatisticsWithNewGoalCreatedUseCase { // TODO MIC look for a better place
        return UpdateStatisticsWithNewGoalCreatedUseCase(repository)
    }


}