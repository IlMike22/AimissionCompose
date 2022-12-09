package com.mind.market.aimissioncompose.data.settings.repository

import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    suspend fun setDeleteGoalsOnStartup(enabled: Boolean)

//    fun getDeleteGoalsOnStartup(): Flow<Boolean>

    suspend fun duplicateGoals(): Boolean

    suspend fun setHideDoneGoals(isHide: Boolean)

    fun getUserSettings(): Flow<Boolean>
}