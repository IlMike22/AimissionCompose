package com.mind.market.aimissioncompose.data.settings.repository

import com.mind.market.aimissioncompose.data.SettingsLocalDataSource

class SettingsRepository(
    private val localDataSource: SettingsLocalDataSource
) : ISettingsRepository {
    //private val localDataSource = SettingsLocalDataSource(context)

    override suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        localDataSource.setDeleteGoalsOnStartup(enabled)
    }

    override fun getDeleteGoalsOnStartup() = localDataSource.getDeleteGoalsOnStartup()

    override suspend fun duplicateGoals() {
        localDataSource.duplicateGoals()
    }
}