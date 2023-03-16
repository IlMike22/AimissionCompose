package com.mind.market.aimissioncompose.presentation.detail

import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.ValidationStatusCode

data class DetailUiState(
    val goal: Goal = Goal.EMPTY,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val ctaButtonText: String = "",
    val hasValidationErrors: Boolean = false,
    val validationCode: ValidationStatusCode? = null
)
