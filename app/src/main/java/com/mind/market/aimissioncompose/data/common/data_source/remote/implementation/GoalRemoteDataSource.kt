package com.mind.market.aimissioncompose.data.common.data_source.remote.implementation

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.data.common.FIREBASE_DATABASE_KEY_STATUS
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_GOAL
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_USER
import com.mind.market.aimissioncompose.data.common.data_source.IGoalDataSource
import com.mind.market.aimissioncompose.data.dto.GoalDto
import com.mind.market.aimissioncompose.data.toGoal
import com.mind.market.aimissioncompose.data.toGoalDto
import com.mind.market.aimissioncompose.data.toStatusData
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status

class GoalRemoteDataSource(
    private val firebaseDatabase: DatabaseReference,
) : IGoalDataSource {
    val TAG = "GoalRemoteDataSource"
    override suspend fun deleteGoal(
        goal: Goal,
        userId: String?,
        onResult: (Boolean) -> Unit
    ) {
        if (userId == null) {
            Log.e(TAG, "Cannot delete remote goal. UserId is null")
            onResult(false)
            return
        }

        firebaseDatabase
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_GOAL)
            .child(goal.id.toString())
            .removeValue()
            .addOnCompleteListener {
                if (it.isComplete) {
                    if (it.isSuccessful) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }
            }
    }

    override suspend fun getGoals(userId: String?, onResult: (Throwable?, List<Goal>) -> Unit) {
        if (userId == null) {
            Log.e(TAG, "Cannot fetch goals. UserId is null")
            onResult(
                Throwable(message = "Goals cannot be loaded. Please try again"),
                emptyList()
            )
            return
        }
        val goalDtos = mutableListOf<GoalDto>()
        firebaseDatabase
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_GOAL)
            .get()
            .addOnSuccessListener { data ->
                for (singleDataSet in data.children) {
                    singleDataSet.getValue(GoalDto::class.java)?.apply {
                        goalDtos.add(this)
                    }
                }
                onResult(null, goalDtos.map { it.toGoal() })
            }.addOnFailureListener { error ->
                onResult(
                    Throwable(message = "Unable to load goals. Details: ${error.message}"),
                    emptyList()
                )
            }
    }

    override suspend fun insertGoal(goal: Goal, userId: String?) {
        if (userId == null) {
            Log.e(TAG, "Cannot add goal. Firebase user id is null")
            return
        }
        firebaseDatabase
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_GOAL)
            .child(goal.id.toString())
            .setValue(goal.toGoalDto())
    }

    override suspend fun update(goal: Goal, userId: String?, onResult: (Boolean) -> Unit) {
        if (userId == null) {
            Log.e(TAG, "Cannot update remote goal. UserId is null")
            onResult(false)
            return
        }

        firebaseDatabase
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(goal.id.toString())
            .setValue(goal.toGoalDto())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(true)
                } else onResult(false)
            }
    }

    override suspend fun deleteAll(userId: String?, onResult: (Boolean) -> Unit) {
        if (userId == null) {
            Log.e(TAG, "Cannot delete all remote goals. UserId is null")
            onResult(false)
            return
        }
        firebaseDatabase
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_GOAL)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true)
                } else onResult(false)
            }
    }

    override suspend fun updateStatus(id: Int, newStatus: Status, userId: String?) {
        if (userId == null) {
            Log.e(TAG, "Cannot update remote goal status. UserId is null")
            return
        }

        firebaseDatabase.child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_GOAL)
            .child(id.toString())
            .child(FIREBASE_DATABASE_KEY_STATUS)
            .setValue(newStatus.toStatusData())
    }

    override suspend fun getGoal(id: Int, userId: String?, onResult: (Throwable?, Goal?) -> Unit) {
        if (userId == null) {
            Log.e(TAG, "Cannot get remote goal. UserId is null")
            onResult(
                Throwable(
                    message = "The goal could not be loaded. Please try again."
                ), null
            )

            return
        }
        firebaseDatabase.child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_GOAL)
            .child(id.toString())
            .get()
            .addOnSuccessListener { data ->
                data.getValue(GoalDto::class.java)?.apply {
                    onResult(null, this.toGoal())
                }
            }
            .addOnFailureListener {
                onResult(it, null)
            }
    }
}