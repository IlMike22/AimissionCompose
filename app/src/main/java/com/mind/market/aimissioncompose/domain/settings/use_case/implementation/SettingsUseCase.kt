package com.example.aimissionlite.domain.settings.use_case.implementation

import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsUseCase(
    val repository: ISettingsRepository
) : ISettingsUseCase {
    override fun getDeleteGoalsOnStartup(): Flow<Boolean> {
        return repository.getDeleteGoalsOnStartup()
    }

    override suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        return repository.setDeleteGoalsOnStartup(enabled)
    }

    override fun getHeaderText(): String {
        return "Define your settings here"
    }
}