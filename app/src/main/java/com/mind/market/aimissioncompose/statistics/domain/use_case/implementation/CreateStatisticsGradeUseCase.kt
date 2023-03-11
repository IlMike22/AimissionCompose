package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import com.mind.market.aimissioncompose.statistics.data.dto.Grade
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateStatisticsGradeUseCase {
    operator fun invoke(entities: List<StatisticsEntity>): List<Grade> {
        val grades = mutableListOf<Grade>()
        CoroutineScope(Dispatchers.IO).launch {
            for (entity in entities) {
                grades.add(generateGradeForCurrentEntity(entity))
            }
        }
        return grades
    }

    private fun generateGradeForCurrentEntity(entity: StatisticsEntity): Grade {
        val amountGoalsCompleted = entity.amountGoalsCompleted
        val amountGoalsCreated = entity.amountGoalsCreated
        return when {
            amountGoalsCompleted == amountGoalsCreated -> Grade.ALL_GOALS_COMPLETED
            amountGoalsCompleted == 0 -> Grade.NO_GOALS_COMPLETED_YET
            amountGoalsCompleted > amountGoalsCreated - 2 -> Grade.NEARLY_ALL_GOALS_COMPLETED
            amountGoalsCompleted > amountGoalsCreated - 4 -> Grade.SOME_GOALS_COMPLETED
            amountGoalsCompleted > amountGoalsCreated - 6 -> Grade.FEW_GOALS_COMPLETED
            else -> Grade.UNDEFINED
        }
    }
}