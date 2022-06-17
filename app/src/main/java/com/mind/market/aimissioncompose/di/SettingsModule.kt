package com.mind.market.aimissioncompose.di

import android.content.Context
import com.mind.market.aimissioncompose.data.settings.repository.SettingsRepository
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.example.aimissionlite.domain.settings.use_case.implementation.SettingsUseCase
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    @Provides
    @Singleton
    fun provideUseCase(repository: ISettingsRepository): ISettingsUseCase {
        return SettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRepository(@ApplicationContext context: Context): ISettingsRepository {
        return SettingsRepository(context)
    }
}