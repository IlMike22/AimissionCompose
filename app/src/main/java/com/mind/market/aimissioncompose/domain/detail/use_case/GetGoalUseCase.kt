package com.mind.market.aimissioncompose.domain.detail.use_case

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal
import kotlinx.coroutines.flow.Flow

class GetGoalUseCase(
    private val repository: IGoalRepository
) {
    suspend operator fun invoke(id: Int): Flow<Resource<Goal>> {
        return repository.getGoal(id, GoalReadWriteOperation.FIREBASE_DATABASE)
    }
}