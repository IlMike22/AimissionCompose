package com.mind.market.aimissioncompose.domain.detail.use_case

import com.mind.market.aimissioncompose.domain.models.Goal

interface IDetailUseCase { // TODO MIC extract all single functions in an own usecase
    suspend fun insert(goal: Goal): Throwable?

    fun isGoalOverdue(goal: Goal): Boolean

}