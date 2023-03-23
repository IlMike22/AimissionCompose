package com.mind.market.aimissioncompose.domain.landing_page.use_case

import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
import com.mind.market.aimissioncompose.presentation.utils.SortingMode

class SetSortingModeUseCase(val repository: ISettingsRepository) {
    suspend operator fun invoke(mode: SortingMode) {
        repository.setSortingMode(mode)
    }
}