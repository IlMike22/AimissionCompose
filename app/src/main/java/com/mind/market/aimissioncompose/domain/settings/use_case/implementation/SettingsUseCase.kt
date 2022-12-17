package com.mind.market.aimissioncompose.domain.settings.use_case.implementation

import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
import com.mind.market.aimissioncompose.data.settings.repository.SettingEntries
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.flow.Flow

class SettingsUseCase(
    val repository: ISettingsRepository
) : ISettingsUseCase {
//    override fun getDeleteGoalsOnStartup(): Flow<Boolean> {
//        return repository.getDeleteGoalsOnStartup()
//    }

    override suspend fun duplicateGoals(): Boolean {
        return repository.duplicateGoals()
    }

//    override suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
//        return repository.setDeleteGoalsOnStartup(enabled)
//    }

    override suspend fun setHideDoneGoals(isHide: Boolean) {
        return repository.setHideDoneGoals(isHide)
    }

    override suspend fun setShowGoalOverdueDialog(show: Boolean) {
        return repository.showGoalOverdueDialog(show)
    }

    override fun getHeaderText(): String {
        return "Define your settings here"
    }

    override fun getUserSettings(): Flow<SettingEntries> {
        return repository.getUserSettings()
    }

    override suspend fun getGoalsComplete(): Flow<Resource<Int>> {
        return repository.getAmountGoalsForStatus(Status.DONE)
    }
}