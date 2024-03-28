package com.mind.market.aimissioncompose.domain.detail.use_case.implementation

import com.mind.market.aimissioncompose.domain.detail.use_case.IDetailUseCase
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal
import java.time.LocalDate
import java.time.LocalDateTime

class DetailUseCase(
    private val repository: IGoalRepository
) : IDetailUseCase {
    override suspend fun insert(goal: Goal):Throwable? = repository.insert(goal)

    override fun isGoalOverdue(goal: Goal): Boolean = goal.finishDate < LocalDateTime.now()


}