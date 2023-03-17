package com.mind.market.aimissioncompose.domain.settings.use_case

import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository

class ShowOverdueDialogUseCase(
    val repository: ISettingsRepository
) {
    suspend operator fun invoke(show: Boolean) {
        repository.showGoalOverdueDialog(show)
    }
}