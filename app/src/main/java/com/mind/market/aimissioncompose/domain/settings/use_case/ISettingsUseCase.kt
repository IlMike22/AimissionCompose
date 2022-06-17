package com.example.aimissionlite.domain.settings.use_case

import kotlinx.coroutines.flow.Flow

interface ISettingsUseCase {
    fun getDeleteGoalsOnStartup(): Flow<Boolean>

    suspend fun setDeleteGoalsOnStartup(enabled: Boolean)

    fun getHeaderText():String
}