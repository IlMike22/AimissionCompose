package com.mind.market.aimissioncompose.di

import android.content.Context
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
import com.mind.market.aimissioncompose.data.settings.repository.SettingsRepository
import com.mind.market.aimissioncompose.domain.settings.use_case.implementation.SettingsUseCase
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
    fun provideSettingsUseCase(repository: ISettingsRepository): ISettingsUseCase {
        return SettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSettingsLocalDataSource(
        @ApplicationContext context: Context,
        database: GoalRoomDatabase
    ): SettingsLocalDataSource {
        return SettingsLocalDataSource(
            context = context,
            goalDao = database.goalDao()
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        localDataSource: SettingsLocalDataSource,
        database: GoalRoomDatabase
    ): ISettingsRepository {
        return SettingsRepository(localDataSource, database.goalDao())
    }
}