package com.mind.market.aimissioncompose.presentation.common

import com.mind.market.aimissioncompose.domain.models.Goal

data class DropDownItem(
    val id: DropDownItemId,
    val name: String,
    val correspondingGoal: Goal? = null
)

enum class DropDownItemId {
    DELETE_GOAL,
    HIDE_GOAL
}
