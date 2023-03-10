package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsOperation
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class UpdateStatisticUseCase(
    private val statisticsRepository: IStatisticsRepository
) {
    suspend operator fun invoke(operation: StatisticsOperation, onResult: (Boolean) -> Unit = {}) {
        val currentDate = LocalDateTime.now()
        statisticsRepository.getEntityByDate(
            currentDate.monthValue,
            currentDate.year
        ) { error, entity ->
            if (error != null) {
                onResult(false)
                return@getEntityByDate
            }
            when (operation) {
                is StatisticsOperation.Add -> {
                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (entity == null) {
                                return@launch
                            }
                            statisticsRepository.updateEntity(
                                update(entity, operation)
                            )
                            onResult(true)
                        }
                    } catch (exception: Exception) {
                        onResult(false)
                    }
                }
                is StatisticsOperation.Update -> {
                    if (operation.newGoal == null || operation.oldGoal == null) {
                        onResult(false)
                        return@getEntityByDate
                    }
                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (entity == null) {
                                return@launch
                            }
                            val update = handleCurrentStatisticsState(entity, operation)
                            statisticsRepository.updateEntity(
                                entity.copy(
                                    amountGoalsCreated = update.first,
                                    amountGoalsStarted = update.second,
                                    amountGoalsCompleted = update.third
                                )
                            )
                        }
                    } catch (exception: Exception) {
                        onResult(false)
                    }
                }
            }
        }
    }

    private fun update(entity: StatisticsEntity, operation: StatisticsOperation): StatisticsEntity {
        return when (operation) {
            is StatisticsOperation.Add -> {
                val currentStatisticsState = handleCurrentStatisticsState(entity, operation)

                entity.copy(
                    amountGoalsCreated = currentStatisticsState.first,
                    amountGoalsStarted = currentStatisticsState.second,
                    amountGoalsCompleted = currentStatisticsState.third,
                    lastUpdated = LocalDateTime.now(),
                )
            }
            else -> entity // currently just return same entity again
        }
    }

    private fun handleCurrentStatisticsState(
        entity: StatisticsEntity,
        operation: StatisticsOperation
    ): Triple<Int, Int, Int> { // TODO MIC remove triple and use data class instead
        val amountGoalsCreated = entity.amountGoalsCreated
        val amountGoalsStarted = entity.amountGoalsStarted
        val amountGoalsCompleted = entity.amountGoalsCompleted
        when (operation) {
            is StatisticsOperation.Add -> {
                return Triple(
                    entity.amountGoalsCreated + 1,
                    entity.amountGoalsStarted,
                    entity.amountGoalsCompleted
                )
            }
            is StatisticsOperation.Update -> {
                if (operation.newGoal == null) {
                    return Triple(
                        entity.amountGoalsCreated,
                        entity.amountGoalsStarted,
                        entity.amountGoalsCompleted
                    )
                }
                return when (operation.newGoal.status) {
                    Status.TODO -> {
                        Triple(amountGoalsCreated, amountGoalsStarted, amountGoalsCompleted)
                    }
                    Status.IN_PROGRESS -> {
                        Triple(amountGoalsCreated, amountGoalsStarted + 1, amountGoalsCompleted)
                    }
                    Status.DONE -> {
                        Triple(amountGoalsCreated, amountGoalsStarted, amountGoalsCompleted + 1)
                    }
                    else -> {
                        Triple(amountGoalsCreated, amountGoalsStarted, amountGoalsCompleted)
                    }
                }

            }
            else -> return Triple(amountGoalsCreated, amountGoalsStarted, amountGoalsCompleted)
        }
    }
}