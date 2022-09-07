package com.mind.market.aimissioncompose.presentation.detail

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.detail.use_case.IDetailUseCase
import com.example.aimissionlite.models.domain.Genre
import com.example.aimissionlite.models.domain.GoalValidationStatusCode
import com.example.aimissionlite.models.domain.Status
import com.example.aimissionlite.models.domain.ValidationStatusCode
import com.mind.market.aimissioncompose.AimissionComposeApplication
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.data.Converters.Companion.toGenreId
import com.mind.market.aimissioncompose.data.Converters.Companion.toPriorityId
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IGoalRepository,
    app: Application
) : AndroidViewModel(app) {
    private val uiEvent = MutableSharedFlow<DetailUIEvent<GoalValidationStatusCode>>()
    private val resourceProvider = getApplication<AimissionComposeApplication>()

    var state by mutableStateOf(DetailState())

    // private val _state = MutableStateFlow(DetailState.ShowEditGoal(Goal.EMPTY))
    //val state = _state.asStateFlow()

    private var currentGoal = Goal.EMPTY
    var buttonText: String = resourceProvider.getString(R.string.button_done)

    val goalTitle = MutableStateFlow<String?>(null)
    val goalDescription = MutableStateFlow<String?>(null)

    var selectedChipGenre: Int? = null
    var selectedChipPriority: Int? = null


//    fun setSelectedChipGroupItem(chipGroup: ChipGroupName, selectedId: Int) {
//        when (chipGroup) {
//            ChipGroupName.GENRE -> {
//                selectedChipGenre = selectedId
//            }
//            ChipGroupName.PRIORITY -> {
//                selectedChipPriority = selectedId
//            }
//        }
//    }

    fun onSaveGoalButtonClicked() {
//        state.goal.apply {
//            if (this != Goal.EMPTY) {
//                updateGoal(
//                    Goal(
//                        id = id,
//                        title = title,
//                        description = description,
//                        creationDate = creationDate,
//                        changeDate = getCurrentDate(),
//                        isRepeated = isRepeated,
//                        genre = Genre.UNKNOWN, //TODO set Genre and Priority everywhere..
//                        status = status,
//                        priority = Priority.UNKNOWN
//                    )
//                )
//            }
//        }

        createNewGoal()
    }

    fun getAndShowGoal(id: Int) = viewModelScope.launch {
        currentGoal = repository.getGoal(id)

        goalTitle.value = currentGoal.title
        goalDescription.value = currentGoal.description
        selectedChipGenre = currentGoal.genre.toGenreId()
        selectedChipPriority = currentGoal.priority.toPriorityId()

        showGoal(currentGoal)
    }

    fun onInfoClicked() {
        navigateToInfo()
    }

    fun onSettingsClicked() {
        navigateToSettings()
    }

    private fun updateGoal(newGoal: Goal) {
        if (newGoal == currentGoal) {
            navigateToMainFragment()
            return
        }

        currentGoal = newGoal

        val validationStatusCode = GoalValidationStatusCode(
            statusCode = isGoalValid(currentGoal),
            isGoalUpdated = true
        )

        if (validationStatusCode.statusCode == ValidationStatusCode.OK) {
            viewModelScope.launch {
                repository.updateGoal(currentGoal)
            }

            navigateToMainFragment()
        }

        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.ShowValidationResult(validationStatusCode))
        }
    }

    private fun createNewGoal() {
        val currentDate = getCurrentDate()

        val newGoal = Goal(
            id = 0,
            title = state.goal.title,
            description = state.goal.description,
            creationDate = currentDate,
            changeDate = currentDate,
            isRepeated = false,
            genre = Genre.HEALTH, // TODO add genre later
            status = Status.TODO,
            priority = Priority.LOW // TODO add prio later
        )

        val goalValidationStatusCode = GoalValidationStatusCode(
            statusCode = isGoalValid(newGoal),
            isGoalUpdated = false
        )


        if (goalValidationStatusCode.statusCode == ValidationStatusCode.OK) {
            viewModelScope.launch {
                repository.insert(newGoal)
            }

            navigateToMainFragment()
        }

        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.ShowValidationResult(goalValidationStatusCode))
        }
    }

    private fun showGoal(goal: Goal) {
        //_state.value = DetailState.ShowEditGoal(goal)
    }

    private fun getCurrentDate() = LocalDateTime.now()

    private fun isGoalValid(goal: Goal): ValidationStatusCode {
        goal.apply {
            return when {
                title.isBlank() -> ValidationStatusCode.NO_TITLE
                description.isBlank() -> ValidationStatusCode.NO_DESCRIPTION
                genre.isGenreNotSet() -> ValidationStatusCode.NO_GENRE
                else -> ValidationStatusCode.OK
            }
        }
    }

    private fun navigateToMainFragment() {
        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.HideKeyboard())
            uiEvent.emit(DetailUIEvent.NavigateToLandingPage())
        }
    }

    private fun navigateToSettings() {
        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.NavigateToSettings())

        }
    }

    private fun navigateToInfo() {
        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.NavigateToInfo())
        }
    }

    companion object {
//        private fun Int.toGenre(): Genre =
//            when (this) {
//                R.id.chip_genre_business -> Genre.BUSINESS
//                R.id.chip_genre_socialising -> Genre.SOCIALISING
//                R.id.chip_genre_fitness -> Genre.FITNESS
//                R.id.chip_genre_money -> Genre.MONEY
//                R.id.chip_genre_partnership -> Genre.PARTNERSHIP
//                R.id.chip_genre_health -> Genre.HEALTH
//                else -> Genre.UNKNOWN
//            }

//        private fun Int.toPriority(): Priority =
//            when (this) {
//                R.id.chip_priority_low -> Priority.LOW
//                R.id.chip_priority_high -> Priority.HIGH
//                else -> Priority.NORMAL
//            }
//    }

        private fun Genre.isGenreNotSet(): Boolean {
            if (this == Genre.UNKNOWN) {
                return true
            }
            return false
        }
    }
}