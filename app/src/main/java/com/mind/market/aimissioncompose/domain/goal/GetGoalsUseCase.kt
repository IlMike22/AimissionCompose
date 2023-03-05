package com.mind.market.aimissioncompose.domain.goal

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal
import kotlinx.coroutines.flow.Flow

class GetGoalsUseCase(
    private val repository: IGoalRepository
) {
    operator fun invoke(): Flow<Resource<List<Goal>>> =
        repository.getGoals(operation = GoalReadWriteOperation.FIREBASE_DATABASE)
}