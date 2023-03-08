import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.data_source.IGoalDataSource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GoalRepository(
    private val goalRemoteDataSource: IGoalDataSource,
    private val goalLocalDataSource: IGoalDataSource,
    private val authRemoteDataSource: IAuthenticationRemoteDataSource
) : IGoalRepository {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(goal: Goal, mode: GoalReadWriteOperation) {
        if (mode == GoalReadWriteOperation.LOCAL_DATABASE) {
            goalLocalDataSource.insertGoal(goal, null)
            return
        }

        goalRemoteDataSource.insertGoal(goal, getFirebaseUserId())
    }

    @WorkerThread
    override suspend fun getGoal(id: Int, operation: GoalReadWriteOperation): Flow<Resource<Goal>> {
        if (operation == GoalReadWriteOperation.LOCAL_DATABASE) {
            return callbackFlow {
                trySend(Resource.Loading(true))
                goalLocalDataSource.getGoal(id) { error, goal ->
                    if (error != null) {
                        trySend(
                            Resource.Error(
                                message = error.message
                                    ?: "Unknown error getting goal from local data source"
                            )
                        )
                    } else {
                        trySend(Resource.Success(data = goal ?: Goal.EMPTY))
                    }
                }
            }
        } else {
            return callbackFlow {
                trySend(Resource.Loading(true))
                goalRemoteDataSource.getGoal(id = id, userId = getFirebaseUserId()) { error, goal ->
                    trySend(Resource.Loading(false))
                    if (error != null) {
                        trySend(
                            Resource.Error(
                                message = error.message
                                    ?: "Unknown error while getting goal from remote data source."
                            )
                        )
                    } else {
                        trySend(Resource.Success(data = goal ?: Goal.EMPTY))
                    }
                }
                awaitClose { }
            }
        }
    }

    @WorkerThread
    @CheckResult
    override suspend fun deleteAll(mode: GoalReadWriteOperation, onResult: (Boolean) -> Unit) {
        if (mode == GoalReadWriteOperation.LOCAL_DATABASE) {
            goalLocalDataSource.deleteAll() { isSuccess ->
                onResult(isSuccess)
            }

        } else {
            goalRemoteDataSource.deleteAll() { isSuccess ->
                onResult(isSuccess)
            }
        }
    }

    @WorkerThread
    override suspend fun updateStatus(
        id: Int,
        status: Status,
        operation: GoalReadWriteOperation,
        onResult: (Status) -> Unit
    ) {
        if (operation == GoalReadWriteOperation.LOCAL_DATABASE) {
            goalLocalDataSource.updateStatus(id, status)
            onResult(status)
        } else {
            goalRemoteDataSource.updateStatus(id, newStatus = status, userId = getFirebaseUserId())
            onResult(status)
        }
    }

    @WorkerThread
    @CheckResult
    override suspend fun deleteGoal(
        goal: Goal,
        onResult: (Boolean) -> Unit,
        mode: GoalReadWriteOperation
    ) {
        if (mode == GoalReadWriteOperation.LOCAL_DATABASE) {
            goalLocalDataSource.deleteGoal(goal) {
                onResult(it)
            }
            return
        }

        goalRemoteDataSource.deleteGoal(
            goal = goal,
            onResult = onResult,
            userId = getFirebaseUserId()
        )
    }

    @WorkerThread
    @CheckResult
    override suspend fun updateGoal(
        goal: Goal,
        operation: GoalReadWriteOperation,
        onResult: (Boolean) -> Unit
    ) {
        if (operation == GoalReadWriteOperation.LOCAL_DATABASE) {
            return try {
                goalLocalDataSource.update(goal) {
                    onResult(true)
                }

            } catch (exception: Exception) {
                onResult(false)
            }
        } else {
            goalRemoteDataSource.update(
                goal = goal,
                userId = getFirebaseUserId()
            ) { isSuccess ->
                onResult(isSuccess)
            }
        }
    }

    override fun getGoals(operation: GoalReadWriteOperation): Flow<Resource<List<Goal>>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            if (operation == GoalReadWriteOperation.LOCAL_DATABASE) {
                goalLocalDataSource.getGoals { error, goals ->
                    trySend(Resource.Loading(false))
                    if (error != null) {
                        trySend(Resource.Error(message = error.message ?: "Unknown error"))
                    } else {
                        trySend(Resource.Success(data = goals))
                    }
                }
            } else {
                goalRemoteDataSource.getGoals(userId = getFirebaseUserId()) { error, goals ->
                    trySend(Resource.Loading(false))
                    if (error != null) {
                        trySend(Resource.Error(message = error.message ?: "Unknown error"))
                    } else {
                        trySend(Resource.Success(data = goals))
                    }
                }
            }

            awaitClose { }
        }
    }

    private fun getFirebaseUserId(): String =
        authRemoteDataSource.getUserData().id
}


