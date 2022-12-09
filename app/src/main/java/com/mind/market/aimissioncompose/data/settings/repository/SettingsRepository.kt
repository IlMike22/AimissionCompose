package com.mind.market.aimissioncompose.data.settings.repository

import com.mind.market.aimissioncompose.data.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val localDataSource: SettingsLocalDataSource
) : ISettingsRepository {
    //private val localDataSource = SettingsLocalDataSource(context)

    override suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        localDataSource.setDeleteGoalsOnStartup(enabled)
    }

//    override fun getDeleteGoalsOnStartup() = localDataSource.getDeleteGoalsOnStartup()

    override suspend fun duplicateGoals(): Boolean {
        return localDataSource.duplicateGoals()
    }

    override suspend fun setHideDoneGoals(isHide: Boolean) {
        localDataSource.setHideDoneGoals(isHide)
    }

    override fun getUserSettings(): Flow<Boolean> {
        return localDataSource.getUserSettings()
    }
}