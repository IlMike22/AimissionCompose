package com.mind.market.aimissioncompose.domain.models

data class GoalListItem(
    val monthValue: String = "",
    val yearValue: String = "",
    val goals: List<Goal> = emptyList()
)