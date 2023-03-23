package com.mind.market.aimissioncompose.data.settings.repository

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.presentation.utils.SortingMode
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    suspend fun setDeleteGoalsOnStartup(enabled: Boolean)
    suspend fun duplicateGoals(): Boolean
    suspend fun setHideDoneGoals(isHide: Boolean)
    suspend fun showGoalOverdueDialog(show: Boolean)
    fun getUserSettings(): Flow<SettingEntries>
    suspend fun getAmountGoalsForStatus(status: Status): Flow<Resource<Int>>
    suspend fun setSortingMode(mode: SortingMode)
}