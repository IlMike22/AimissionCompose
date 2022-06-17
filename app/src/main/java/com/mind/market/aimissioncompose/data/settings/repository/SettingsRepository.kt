package com.mind.market.aimissioncompose.data.settings.repository

import android.content.Context
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository

class SettingsRepository(context: Context) : ISettingsRepository {
    private val localDataSource = SettingsLocalDataSource(context)

    override suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        localDataSource.setDeleteGoalsOnStartup(enabled)
    }

    override fun getDeleteGoalsOnStartup() = localDataSource.getDeleteGoalsOnStartup()
}