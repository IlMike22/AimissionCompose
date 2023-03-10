package com.mind.market.aimissioncompose.domain.detail.use_case.implementation

import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsOperation
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class UpdateStatisticsUseCase(
    private val statisticsRepository: IStatisticsRepository
) {
    suspend operator fun invoke(operation: StatisticsOperation, onResult: (Boolean) -> Unit = {}) {
        when (operation) {
            is StatisticsOperation.Add -> {
                val currentDate = LocalDateTime.now()

                statisticsRepository.getStatisticsEntityByDate(
                    currentDate.monthValue,
                    currentDate.year
                ) { error, entity ->
                    if (error != null) {
                        onResult(false)
                        return@getStatisticsEntityByDate
                    }

                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (entity == null) {
                                return@launch
                            }
                            statisticsRepository.updateStatisticEntity(
                                entity.copy(
                                    amountGoalsCreated = entity.amountGoalsCreated + 1
                                )
                            )
                            onResult(true)
                        }
                    } catch (exception: Exception) {
                        onResult(false)
                    }
                }
            }
        }
    }
}