package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository

class DoesStatisticExistsUseCase(
    private val repository: IStatisticsRepository
) {
    suspend operator fun invoke(
        month: Int,
        year: Int,
        onResult: (Boolean) -> Unit
    ) {
        val id = "$month$year"
        repository.getEntity(id) { _, entity ->
            onResult(entity != null)
        }
    }
}