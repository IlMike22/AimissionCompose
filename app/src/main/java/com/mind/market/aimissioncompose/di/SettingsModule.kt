package com.mind.market.aimissioncompose.di

import android.content.Context
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
import com.mind.market.aimissioncompose.data.settings.repository.SettingsRepository
import com.mind.market.aimissioncompose.domain.settings.use_case.GetUserSettingsUseCase
import com.mind.market.aimissioncompose.domain.settings.use_case.HideDoneGoalsUseCase
import com.mind.market.aimissioncompose.domain.settings.use_case.ShowOverdueDialogUseCase
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

    @Provides
    @Singleton
    fun provideHideDoneGoalsUseCase(repository: ISettingsRepository): HideDoneGoalsUseCase {
        return HideDoneGoalsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideShowOverdueDialogUseCase(repository: ISettingsRepository): ShowOverdueDialogUseCase {
        return ShowOverdueDialogUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserSettingsUseCase(repository: ISettingsRepository): GetUserSettingsUseCase {
        return GetUserSettingsUseCase(repository)
    }
}