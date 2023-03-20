package com.mind.market.aimissioncompose.data.goal.source.local

import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.dto.GoalDto
import com.mind.market.aimissioncompose.domain.models.Status

class FakeGoalDao: IGoalDao {
    override suspend fun getGoals(): List<GoalDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getGoal(id: Int): GoalDto {
        TODO("Not yet implemented")
    }

    override suspend fun updateStatus(id: Int, status: String) {
        TODO("Not yet implemented")
    }

    override suspend fun update(goal: GoalDto) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(goal: GoalDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGoal(goal: GoalDto) {
        TODO("Not yet implemented")
    }

    override suspend fun getAmountGoalsForStatus(status: Status): Int {
        TODO("Not yet implemented")
    }
}