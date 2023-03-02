package com.mind.market.aimissioncompose.data.common.data_source

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.dto.GoalDto
import com.mind.market.aimissioncompose.data.toGoal
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

    override suspend fun getGoal(id: Int, userId: String, onResult: (Throwable?, Goal?) -> Unit) {
        firebaseDatabase.child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(id.toString())
            .get()
            .addOnSuccessListener { data ->
                println("!! GET goal was successful. Data is $data")
                data.getValue(GoalDto::class.java)?.apply {
                    onResult(null, this.toGoal())
                }
            }
            .addOnFailureListener {
                println("!! GET goal failed. Error: ${it.message}")
                onResult(it, null)
            }
    }

    companion object {
        private const val FIREBASE_TABLE_USER = "users"
    }
}