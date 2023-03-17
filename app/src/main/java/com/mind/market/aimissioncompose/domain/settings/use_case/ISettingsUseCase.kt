package com.mind.market.aimissioncompose.domain.settings.use_case

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.settings.repository.SettingEntries
import kotlinx.coroutines.flow.Flow

interface ISettingsUseCase {
//    fun getDeleteGoalsOnStartup(): Flow<Boolean>

    suspend fun duplicateGoals(): Boolean

//    suspend fun setDeleteGoalsOnStartup(enabled: Boolean)

    suspend fun setHideDoneGoals(isHide: Boolean)
    suspend fun setShowGoalOverdueDialog(show:Boolean)

    fun getHeaderText(): String

    fun getUserSettings(): Flow<SettingEntries>

    suspend fun getGoalsComplete(): Flow<Resource<Int>>
}