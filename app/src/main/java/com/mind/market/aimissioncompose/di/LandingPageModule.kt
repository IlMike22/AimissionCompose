package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.domain.landing_page.use_case.ILandingPageUseCase
import com.mind.market.aimissioncompose.domain.landing_page.use_case.implementation.LandingPageUseCase
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
    fun provideUseCase(): ILandingPageUseCase {
        return LandingPageUseCase()
    }
}