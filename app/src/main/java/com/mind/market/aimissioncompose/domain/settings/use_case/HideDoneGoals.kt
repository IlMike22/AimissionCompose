package com.mind.market.aimissioncompose.domain.settings.use_case

import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository

class HideDoneGoalsUseCase(val repository: ISettingsRepository) {
    suspend operator fun invoke(hideGoals: Boolean) {
        repository.setHideDoneGoals(hideGoals)
    }
}