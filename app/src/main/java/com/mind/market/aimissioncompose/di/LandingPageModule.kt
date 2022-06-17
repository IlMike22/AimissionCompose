package com.mind.market.aimissioncompose.di

import com.example.aimissionlite.domain.landing_page.use_case.implementation.LandingPageUseCase
import com.example.aimissionlite.domain.landing_page.use_case.ILandingPageUseCase
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
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
        settingsRepository: ISettingsRepository
    ): ILandingPageUseCase =
        LandingPageUseCase(
            goalRepository = goalRepository,
            settingsRepository = settingsRepository
        )
}