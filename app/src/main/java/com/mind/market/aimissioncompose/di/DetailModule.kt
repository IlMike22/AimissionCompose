package com.mind.market.aimissioncompose.di


import com.example.aimissionlite.domain.detail.use_case.IDetailUseCase
import com.example.aimissionlite.domain.detail.use_case.implementation.DetailUseCase
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
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
}