import android.util.Log
import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.common.data_source.IGoalRemoteDataSource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.data.dto.GoalDto
import com.mind.market.aimissioncompose.data.toGoal
import com.mind.market.aimissioncompose.data.toGoalDto
import com.mind.market.aimissioncompose.data.toStatusData
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException

//TODO add GoalLocalDataSource because it`s not clear to go directly to Dao in Repo
class GoalRepository(
    private val goalDao: IGoalDao, // TODO MIC add local ds or remove local data storage
    private val firebaseDatabase: DatabaseReference,
    private val goalRemoteDataSource: IGoalRemoteDataSource,
    private val authRemoteDataSource: IAuthenticationRemoteDataSource
) : IGoalRepository {
    val TAG = "GoalRepository"

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(goal: Goal, mode: GoalReadWriteOperation) {
        /**
        First we map the domain model to data before we use the dao to write the new goal into db.
         */

        goalRemoteDataSource.insertGoal(goal, mode, getFirebaseUserId())

    }

    @WorkerThread
    override suspend fun getGoal(id: Int, operation: GoalReadWriteOperation): Flow<Resource<Goal>> {
//        if (operation == GoalReadWriteOperation.LOCAL_DATABASE) {
//            goalDao.getGoal(id).toGoal()
//        } else {
        return callbackFlow {
            trySend(Resource.Loading(true))
            goalRemoteDataSource.getGoal(id, getFirebaseUserId()) { error, goal ->
                trySend(Resource.Loading(false))
                if (error != null) {
                    trySend(Resource.Error(message = error.message ?: "Unknown error"))
                } else {
                    trySend(Resource.Success(data = goal ?: Goal.EMPTY))
                }
            }
            awaitClose { }
//            }
        }
    }


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
    override suspend fun deleteGoal(goal: Goal, mode: GoalReadWriteOperation) {
        goalRemoteDataSource.deleteGoal(goal, mode, getFirebaseUserId())
//        goalDao.deleteGoal(goal.toGoalDto())
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
            return callbackFlow {
                trySend(Resource.Loading(true))
                val userId = getFirebaseUserId()
                val goalDtos = mutableListOf<GoalDto>()
                firebaseDatabase // TODO MIC extract in GoalRemoteDS
                    .child("users")
                    .child(userId)
                    .get()
                    .addOnSuccessListener { data ->
                        for (singleDataSet in data.children) {
                            singleDataSet.getValue(GoalDto::class.java)?.apply {
                                goalDtos.add(this)
                            }
                        }
                        Log.i(TAG, "!! data fetched. data is $goalDtos")
                        trySend(Resource.Loading(false))
                        trySend(Resource.Success(
                            goalDtos.map { goalDto ->
                                goalDto.toGoal()
                            }
                        ))
                    }.addOnFailureListener { error ->
                        trySend(Resource.Loading(false))
                        Log.i(TAG, "!! data fetched failed. error is ${error.message}")
                        trySend(Resource.Error(message = error.message ?: "Unknown error"))
                    }

                awaitClose { }
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
        authRemoteDataSource.getUserData().id
}


