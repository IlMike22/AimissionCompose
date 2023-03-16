package com.mind.market.aimissioncompose.domain.models

data class GoalValidationStatusCode(
    val statusCode: ValidationStatusCode = ValidationStatusCode.UNKNOWN,
    val isGoalUpdated: Boolean = false
) {
    companion object {
        val EMPTY = GoalValidationStatusCode(
            statusCode = ValidationStatusCode.UNKNOWN,
            isGoalUpdated = false
        )
    }
}


enum class ValidationStatusCode {
    NO_TITLE,
    NO_DESCRIPTION,
    NO_GENRE,
    NO_PRIORITY,
    DUE_DATE_IS_IN_PAST,
    OK,
    UNKNOWN
}