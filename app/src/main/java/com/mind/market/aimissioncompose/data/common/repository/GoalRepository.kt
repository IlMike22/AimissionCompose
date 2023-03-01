package com.mind.market.aimissioncompose.data.common.repository

import android.util.Log
import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.dto.GoalDto
import com.mind.market.aimissioncompose.data.toGoal
import com.mind.market.aimissioncompose.data.toGoalDto
import com.mind.market.aimissioncompose.data.toStatusData
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import kotlin.random.Random

//TODO add GoalLocalDataSource because it`s not clear to go directly to Dao in Repo

class GoalRepository(
    private val goalDao: IGoalDao,
    private val firebaseDatabase: DatabaseReference,
    private val authRemoteDatasource: IAuthenticationRemoteDataSource
) : IGoalRepository {
    val TAG = "GoalRepository"

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(goal: Goal, mode: GoalReadWriteOperation) {
        /**
        First we map the domain model to data before we use the dao to write the new goal into db.
         */
        if (mode == GoalReadWriteOperation.LOCAL_DATABASE) {
            goalDao.insert(goal.toGoalDto())
        } else {
            // TODO MIC just for test, refactor later
            val userId = getFirebaseUserId()
            firebaseDatabase
                .child(FIREBASE_TABLE_USER)
                .child(userId)
                .child(Random.nextInt(0, 10_000).toString())
                .setValue(goal.toGoalDto())
        }
    }

    @WorkerThread
    override suspend fun getGoal(id: Int): Goal = goalDao.getGoal(id).toGoal()

    @WorkerThread
    @CheckResult
    override suspend fun deleteAll(): Boolean {
        return try {
            val result = goalDao.deleteAll()
            println("!! Success on deleting all goals. Details: $result")
            true
        } catch (error: Throwable) {
            println("!! Error while deleting all goals. Details: ${error.message}")
            false
        }
    }

    @WorkerThread
    override suspend fun updateStatus(id: Int, status: Status) {
        goalDao.updateStatus(
            id = id,
            status = status.toStatusData()
        )
    }

    @WorkerThread
    @CheckResult
    override suspend fun deleteGoal(goal: Goal) {
        goalDao.deleteGoal(goal.toGoalDto())

    }

    @WorkerThread
    @CheckResult
    override suspend fun updateGoal(goal: Goal): Boolean {
        return try {
            goalDao.update(goal.toGoalDto())
            true
        } catch (exception: Exception) {
            false
        }
    }

    override fun getGoals(operation: GoalReadWriteOperation): Flow<Resource<List<Goal>>> {
        if (operation == GoalReadWriteOperation.FIREBASE_DATABASE) {
            return flow {
                emit(Resource.Loading(true))
                val userId = getFirebaseUserId()
                val goalDtos = mutableListOf<GoalDto>()
                firebaseDatabase
                    .child("users")
                    .child(userId)
                    .get()
                    .addOnSuccessListener { data ->
                        // TODO MIC next time: try to emit success and error cases although you are
                        // on another coroutine when unsing it it success and error firebase listener
                        callbackFlow<Resource<List<Goal>>> {
                            for (singleDataSet in data.children) {
                                singleDataSet.getValue(GoalDto::class.java)?.apply {
                                    goalDtos.add(this)
                                }
                            }
                            Log.i(TAG, "!! data fetched. data is $goalDtos")
                            emit(Resource.Loading(false))
                            emit(Resource.Success(
                                goalDtos.map { goalDto ->
                                    goalDto.toGoal()
                                }
                            ))
                        }
                    }.addOnFailureListener { error ->
                        callbackFlow<Resource<List<Goal>>> {
                            emit(Resource.Loading(false))
                            Log.i(TAG, "!! data fetched failed. error is ${error.message}")
                            emit(Resource.Error(message = error.message ?: "Unknown error"))
                        }
                    }
            }
        }

        return flow {
            emit(Resource.Loading(true))
            try {
                val goals = goalDao.getGoals()
                emit(Resource.Success(
                    goals.let { goalsDto ->
                        goalsDto.map { goalDto ->
                            goalDto.toGoal()
                        }
                    }
                ))
            } catch (exception: IOException) {
                exception.printStackTrace()
                emit(Resource.Error(message = "Could not load data."))
            }
            emit(Resource.Loading(false))
        }
    }

    private fun getFirebaseUserId(): String =
        authRemoteDatasource.getUserData().id

    companion object {
        private const val FIREBASE_TABLE_USER = "users"
    }
}