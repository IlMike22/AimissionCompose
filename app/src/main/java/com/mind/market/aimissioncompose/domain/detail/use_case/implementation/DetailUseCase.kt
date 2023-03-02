package com.mind.market.aimissioncompose.domain.detail.use_case.implementation

import com.mind.market.aimissioncompose.domain.detail.use_case.IDetailUseCase
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal
import java.time.LocalDate

class DetailUseCase(
    private val repository: IGoalRepository
) : IDetailUseCase {
//    override suspend fun getGoal(id: Int): Goal =
//        repository.getGoal(id)

    override suspend fun updateGoal(goal: Goal): Boolean =
        repository.updateGoal(goal)

    override suspend fun insert(goal: Goal) =
        repository.insert(goal)

    override fun isGoalOverdue(goal: Goal): Boolean = goal.finishDate < LocalDate.now()


}