package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.statistics.data.dto.Grade
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.time.Month

class GenerateStatisticsUseCase(
    private val repository: IStatisticsRepository,
    private val goalRepository: IGoalRepository
) {
    suspend operator fun invoke(): List<StatisticsEntity> {
        val entities = mutableListOf<StatisticsEntity>()
        val goalsResult = goalRepository.getGoals(GoalReadWriteOperation.FIREBASE_DATABASE)
        val createdDates = mutableListOf<LocalDateTime>()

        goalsResult.apply {
            goalsResult.collectLatest { result ->
                if (result is Resource.Success) {
                    val goals = result.data ?: emptyList()
                    goals.forEach { goal ->
                        createdDates.add(goal.creationDate)
                    }
                    val entities = generateStatisticsEntitiesContent(createdDates)
                    generateStatisticEntityData(goals, entities)
                }
                println("!! entities are $entities")
            }
        }

        return entities
    }

    private fun generateStatisticsEntitiesContent(
        createdDates: List<LocalDateTime>
    ): Map<String, String> {
        val entitiesContent = mutableMapOf<String, String>()


        createdDates.forEach { createdDate ->
            entitiesContent[createStatisticsEntityId(createdDate)] =
                getMonthAsString(createdDate.month)
        }
        entitiesContent.forEach { entry ->
            val entityTotalAmounts = mutableMapOf<String, Int>()
            entityTotalAmounts[entry.key] = 0
        }

        return entitiesContent
    }

    private fun generateStatisticEntityData(
        goals: List<Goal>,
        entities: Map<String, String>
    ): List<StatisticsEntity> {
        val goalsPerEntityId = mutableMapOf<String, MutableList<Goal>>()
        val statisticEntities = mutableListOf<StatisticsEntity>()
        entities.forEach { entity ->
            val entityId = entity.key
            goalsPerEntityId.set(entityId, mutableListOf())

            goals.forEach { goal ->
                if (createStatisticsEntityId(goal.creationDate) == entityId) {
                    goalsPerEntityId[entityId]?.add(goal)
                }
            }
        }

        goalsPerEntityId.forEach {
            val totalAmount = it.value.size
            var totalAmountGoalsCompleted = 0
            var totalAmountGoalsInProgress = 0
            var totalAmountGoalsToDo = 0
            it.value.forEach { goal ->
                when (goal.status) {
                    Status.DONE -> {
                        totalAmountGoalsCompleted++
                    }
                    Status.IN_PROGRESS -> {
                        totalAmountGoalsInProgress++
                    }
                    Status.TODO -> {
                        totalAmountGoalsToDo++
                    }
                    Status.DEPRECATED -> {}
                    Status.UNKNOWN -> {}
                }
            }
            statisticEntities.add(
                StatisticsEntity(
                    id = it.key,
                    title = entities[it.key] ?: "",
                    amountGoalsCompleted = totalAmountGoalsCompleted,
                    amountGoalsCreated = totalAmount,
                    amountGoalsStarted = totalAmountGoalsInProgress,
                    amountGoalsNotCompleted = totalAmount - totalAmountGoalsCompleted,
                    created = LocalDateTime.now(),
                    grade = Grade.UNDEFINED, // TODO MIC create later
                    month = 0,
                    year = 0,
                    lastUpdated = LocalDateTime.now() // TODO MIC check if needed
                )
            )
        }

        return statisticEntities
    }

    private fun createStatisticsEntityId(createdDate: LocalDateTime) =
        createdDate.monthValue.toString() + createdDate.year.toString()

    private fun getMonthAsString(month: Month?): String {
        if (month == null) {
            return "Undefined"
        }
        return when (month) {
            Month.JANUARY -> "January"
            Month.FEBRUARY -> "February"
            Month.MARCH -> "March"
            Month.APRIL -> "April"
            Month.MAY -> "May"
            Month.JUNE -> "June"
            Month.JULY -> "July"
            Month.AUGUST -> "August"
            Month.SEPTEMBER -> "September"
            Month.OCTOBER -> "October"
            Month.NOVEMBER -> "Novembre"
            Month.DECEMBER -> "Decembre"
        }
    }
}