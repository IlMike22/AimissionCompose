package com.mind.market.aimissioncompose.data.common.data_source

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.toGoalDto
import com.mind.market.aimissioncompose.domain.models.Goal

class GoalRemoteDataSource(
    private val firebaseDatabase: DatabaseReference,
    private val goalDao: IGoalDao,
) : IGoalRemoteDataSource {
    override suspend fun deleteGoal(goal: Goal, mode: GoalReadWriteOperation, userId: String) {
        if (mode == GoalReadWriteOperation.LOCAL_DATABASE) {
            goalDao.deleteGoal(goal.toGoalDto())
        } else {
            firebaseDatabase
                .child(FIREBASE_TABLE_USER)
                .child(userId)
                .child(goal.id.toString())
                .removeValue()
        }
    }

    override suspend fun insertGoal(goal: Goal, mode: GoalReadWriteOperation, userId: String) {
        if (mode == GoalReadWriteOperation.LOCAL_DATABASE) {
            goalDao.insert(goal.toGoalDto())
        } else {
            firebaseDatabase
                .child(FIREBASE_TABLE_USER)
                .child(userId)
                .child(goal.id.toString())
                .setValue(goal.toGoalDto())
        }
    }

    companion object {
        private const val FIREBASE_TABLE_USER = "users"
    }
}