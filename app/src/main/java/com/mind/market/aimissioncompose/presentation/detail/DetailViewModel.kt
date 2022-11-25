package com.mind.market.aimissioncompose.presentation.detail

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.models.domain.GoalValidationStatusCode
import com.example.aimissionlite.models.domain.Status
import com.example.aimissionlite.models.domain.ValidationStatusCode
import com.mind.market.aimissioncompose.AimissionComposeApplication
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IGoalRepository,
    savedStateHandle: SavedStateHandle,
    app: Application
) : AndroidViewModel(app) {
    private val resourceProvider = getApplication<AimissionComposeApplication>()
    private val goalId: Int = checkNotNull(savedStateHandle[ARGUMENT_GOAL_ID])

    init {
        if (goalId != -1) {
            getAndShowGoal(goalId)
        }
    }

    private var _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state

    private val _uiEvent = Channel<DetailUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentGoal = Goal.EMPTY

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnTitleChanged -> {
                _state.value = _state.value.copy(
                    title = event.title
                )
            }
            is DetailEvent.OnDescriptionChanged -> {
                _state.value = _state.value.copy(
                    description = event.description
                )
            }
            is DetailEvent.OnPriorityChanged -> {
                _state.value = _state.value.copy(
                    priority = event.priority
                )
            }
            is DetailEvent.OnGenreChanged -> {
                _state.value = _state.value.copy(
                    genre = event.genre
                )
            }
            is DetailEvent.OnStatusChanged -> {
                _state.value = _state.value.copy(
                    status = event.status
                )
            }
            is DetailEvent.OnFinishDateChanged -> {
                _state.value = _state.value.copy(
                    finishDate = event.finishDate
                )
            }
            is DetailEvent.OnSaveButtonClicked -> {
                if (goalId != -1) {
                    updateGoal(
                        Goal(
                            id = goalId,
                            title = state.value.title,
                            description = state.value.description,
                            creationDate = state.value.createdDate,
                            changeDate = getCurrentDate(),
                            isRepeated = state.value.isRepeated,
                            genre = state.value.genre,
                            status = state.value.status,
                            priority = state.value.priority,
                            finishDate = state.value.finishDate
                        )
                    )
                    return
                }
                createNewGoal()
            }
        }
    }

    fun getAndShowGoal(id: Int) = viewModelScope.launch {
        currentGoal = repository.getGoal(id)
        showGoal(currentGoal)
    }

    fun onInfoClicked() {
        navigateToInfo()
    }

    fun onSettingsClicked() {
        navigateToSettings()
    }

    private fun updateGoal(newGoal: Goal) {
        currentGoal = newGoal

        val validationStatusCode = GoalValidationStatusCode(
            statusCode = isGoalValid(currentGoal),
            isGoalUpdated = true
        )

        if (validationStatusCode.statusCode == ValidationStatusCode.OK) {
            viewModelScope.launch {
                repository.updateGoal(currentGoal)
                navigateToMainFragment()
            }
        }
    }

    private fun createNewGoal() {
        val newGoal = Goal(
            id = 0,
            title = state.value.title,
            description = state.value.description,
            creationDate = state.value.createdDate,
            changeDate = getCurrentDate(),
            isRepeated = state.value.isRepeated,
            genre = state.value.genre,
            status = Status.TODO,
            priority = state.value.priority,
            finishDate = state.value.finishDate
        )

        val goalValidationStatusCode = GoalValidationStatusCode(
            statusCode = isGoalValid(newGoal),
            isGoalUpdated = false
        )


        if (goalValidationStatusCode.statusCode == ValidationStatusCode.OK) {
            viewModelScope.launch {
                repository.insert(newGoal)
                _uiEvent.send(DetailUIEvent.NavigateToLandingPage) //TODO has to be NavigateUp plus Invalidation
            }
        }
    }

    private fun showGoal(goal: Goal) {
        goal.apply {
            _state.value = DetailState(
                title = title,
                description = description,
                genre = genre,
                status = status,
                priority = priority,
                isRepeated = isRepeated,
                finishDate = finishDate
            )
        }
    }

    private fun getCurrentDate() = LocalDateTime.now()

    private fun isGoalValid(goal: Goal): ValidationStatusCode {
        goal.apply {
            return when {
                title.isBlank() -> ValidationStatusCode.NO_TITLE
                description.isBlank() -> ValidationStatusCode.NO_DESCRIPTION
                genre.isGenreNotSet() -> ValidationStatusCode.NO_GENRE
                priority.isPriorityNotSet() -> ValidationStatusCode.NO_PRIORITY
                else -> ValidationStatusCode.OK
            }
        }
    }

    private fun navigateToMainFragment() {
        viewModelScope.launch {
            _uiEvent.send(DetailUIEvent.HideKeyboard)
            _uiEvent.send(DetailUIEvent.NavigateToLandingPage)
        }
    }

    private fun navigateToSettings() {
        viewModelScope.launch {
            _uiEvent.send(DetailUIEvent.NavigateToSettings)

        }
    }

    private fun navigateToInfo() {
        viewModelScope.launch {
            _uiEvent.send(DetailUIEvent.NavigateToInfo)
        }
    }

    companion object {
        const val ARGUMENT_GOAL_ID = "goalId"
        private fun Genre.isGenreNotSet(): Boolean = (this == Genre.UNKNOWN)
        private fun Priority.isPriorityNotSet() = this == Priority.UNKNOWN
    }
}