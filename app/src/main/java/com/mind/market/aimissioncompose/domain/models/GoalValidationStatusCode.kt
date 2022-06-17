package com.example.aimissionlite.models.domain

data class GoalValidationStatusCode(
    val statusCode: ValidationStatusCode = ValidationStatusCode.UNKNOWN,
    val isGoalUpdated:Boolean = false
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
    OK,
    UNKNOWN
}