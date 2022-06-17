package com.mind.market.aimissioncompose.data

import androidx.room.*
import com.example.aimissionlite.models.domain.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface IGoalDao {
    @Query("SELECT * FROM goal_table")
    fun getGoals(): Flow<List<Goal>>

    @Query("SELECT * FROM goal_table WHERE id = :id")
    suspend fun getGoal(id:Int):Goal

    @Query("UPDATE goal_table SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(goal:Goal)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(goal: Goal)

    @Query("DELETE FROM goal_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteGoal(goal: Goal)
}