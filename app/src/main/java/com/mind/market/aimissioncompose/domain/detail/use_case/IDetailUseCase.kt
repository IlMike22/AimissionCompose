package com.example.aimissionlite.domain.detail.use_case

import com.example.aimissionlite.models.domain.Goal

interface IDetailUseCase {
    suspend fun getGoal(id: Int): Goal
    suspend fun updateGoal(goal: Goal): Boolean
    suspend fun insert(goal: Goal)
}