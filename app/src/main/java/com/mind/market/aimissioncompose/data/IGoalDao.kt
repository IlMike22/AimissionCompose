package com.mind.market.aimissioncompose.data

import androidx.room.*
import com.mind.market.aimissioncompose.data.dto.GoalDto

@Dao
interface IGoalDao {
    @Query("SELECT * FROM goal_table")
    suspend fun getGoals(): List<GoalDto>

    @Query("SELECT * FROM goal_table WHERE id = :id")
    suspend fun getGoal(id: Int): GoalDto

    @Query("UPDATE goal_table SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(goal: GoalDto)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(goal: GoalDto)

    @Query("DELETE FROM goal_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteGoal(goal: GoalDto)
}