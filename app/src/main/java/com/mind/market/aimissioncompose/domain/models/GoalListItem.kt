package com.mind.market.aimissioncompose.domain.models

import java.time.Month

data class GoalListItem(
    val year: Int = 0,
    val month: Month = Month.JANUARY,
    val monthValue: String = "",
    val yearValue: String = "",
    val goals: List<Goal> = emptyList()
) {
    companion object {
        val EMPTY = GoalListItem(0, Month.JANUARY, "", "", emptyList())
    }
}