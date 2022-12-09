package com.example.aimissionlite.domain.settings.use_case

import kotlinx.coroutines.flow.Flow

interface ISettingsUseCase {
//    fun getDeleteGoalsOnStartup(): Flow<Boolean>

    suspend fun duplicateGoals(): Boolean

//    suspend fun setDeleteGoalsOnStartup(enabled: Boolean)

    suspend fun setHideDoneGoals(isHide: Boolean)

    fun getHeaderText(): String

    fun getUserSettings(): Flow<Boolean>
}