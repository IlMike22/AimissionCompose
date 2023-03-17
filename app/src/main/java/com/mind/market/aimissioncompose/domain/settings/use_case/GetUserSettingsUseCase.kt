package com.mind.market.aimissioncompose.domain.settings.use_case

import com.mind.market.aimissioncompose.data.settings.UserSettings
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
import com.mind.market.aimissioncompose.data.settings.repository.SettingEntries
import kotlinx.coroutines.flow.Flow

class GetUserSettingsUseCase(
    val repository: ISettingsRepository
) {
    operator fun invoke(): Flow<SettingEntries> {
        return repository.getUserSettings()
    }
}