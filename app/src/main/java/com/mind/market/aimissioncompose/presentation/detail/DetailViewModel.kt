package com.mind.market.aimissioncompose.presentation.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.AimissionComposeApplication
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.goal.GetGoalUseCase
import com.mind.market.aimissioncompose.domain.goal.InsertGoalUseCase
import com.mind.market.aimissioncompose.domain.goal.UpdateGoalUseCase
import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.domain.models.ValidationStatusCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getGoal: GetGoalUseCase,
    private val insertGoal: InsertGoalUseCase,
    private val updateGoal: UpdateGoalUseCase,
    savedStateHandle: SavedStateHandle,
    app: Application
) : AndroidViewModel(app) {
    private val resourceProvider = getApplication<AimissionComposeApplication>()
    private val goalId: Int = checkNotNull(savedStateHandle[ARGUMENT_GOAL_ID])

    private val _state = MutableStateFlow(DetailUiState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        DetailUiState()
    )

    private val _uiEvent = Channel<DetailUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var auth: FirebaseAuth = Firebase.auth

    init {
        if (goalId != -1) {
            getAndShowGoal(goalId)
        } else {
            _state.update {
                it.copy(
                    ctaButtonText = resourceProvider.getString(R.string.button_text_add),
                    isUserAuthenticated = auth.currentUser != null
                )
            }
        }
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnTitleChanged -> {
                _state.update {
                    it.copy(
                        goal = state.value.goal.copy(
                            title = event.title
                        )
                    )
                }
            }

            is DetailEvent.OnDescriptionChanged -> {
                _state.update {
                    it.copy(
                        goal = state.value.goal.copy(
                            description = event.description
                        )
                    )
                }
            }

            is DetailEvent.OnPriorityChanged -> {
                _state.update {
                    it.copy(
                        goal = state.value.goal.copy(
                            priority = event.priority
                        )
                    )
                }
            }

            is DetailEvent.OnGenreChanged -> {
                _state.update {
                    it.copy(
                        goal = state.value.goal.copy(
                            genre = event.genre
                        )
                    )
                }
            }

            is DetailEvent.OnStatusChanged -> {
                _state.update {
                    it.copy(
                        goal = state.value.goal.copy(
                            status = event.status
                        )
                    )
                }
            }

            is DetailEvent.OnFinishDateChanged -> {
                _state.update {
                    it.copy(
                        goal = state.value.goal.copy(
                            finishDate = event.finishDate
                        )
                    )
                }
            }

            is DetailEvent.OnSaveButtonClicked -> {
                if (goalId != -1) {
                    updateGoal(
                        Goal(
                            id = goalId,
                            title = state.value.goal.title,
                            description = state.value.goal.description,
                            creationDate = state.value.goal.creationDate,
                            changeDate = getCurrentDate(),
                            isRepeated = state.value.goal.isRepeated,
                            genre = state.value.goal.genre,
                            status = state.value.goal.status,
                            priority = state.value.goal.priority,
                            finishDate = state.value.goal.finishDate
                        )
                    )
                    return
                }
                createGoal()
            }
        }
    }

    private fun getAndShowGoal(id: Int) {
        viewModelScope.launch {
            getGoal(id).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                goal = response.data ?: Goal.EMPTY,
                                ctaButtonText = resourceProvider.getString(R.string.button_text_update),
                                isLoading = false,
                                isUserAuthenticated = auth.currentUser != null
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                errorMessage = "Error fetching the goal details. Please try again.",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun onInfoClicked() {
        navigateToInfo()
    }

    fun onSettingsClicked() {
        navigateToSettings()
    }

    private fun updateGoal(newGoal: Goal) {
        val validationCode = isGoalValid(newGoal)
        if (validationCode != ValidationStatusCode.OK) {
            updateValidationErrorUi(validationCode)

        }
        _state.update {
            it.copy(
                hasValidationErrors = false,
                validationCode = null
            )
        }
        updateGoalAndNavigateBack()
    }

    private fun updateValidationErrorUi(code: ValidationStatusCode) {
        _state.update {
            it.copy(
                hasValidationErrors = true,
                validationCode = code
            )
        }
    }

    private fun updateGoalAndNavigateBack() {
        viewModelScope.launch {
            updateGoal(_state.value.goal)
        }
    }

    private fun createGoal() {
        val newGoal = Goal(
            id = Random.nextInt(0, 10_000),
            title = state.value.goal.title,
            description = state.value.goal.description,
            creationDate = state.value.goal.creationDate,
            changeDate = getCurrentDate(),
            isRepeated = state.value.goal.isRepeated,
            genre = state.value.goal.genre,
            status = Status.TODO,
            priority = state.value.goal.priority,
            finishDate = state.value.goal.finishDate
        )

        val validationCode = isGoalValid(newGoal)
        if (validationCode != ValidationStatusCode.OK) {
            updateValidationErrorUi(validationCode)
            return
        }
        viewModelScope.launch {
            val error = insertGoal(newGoal)
            if (error == null) {
                _uiEvent.send(DetailUIEvent.NavigateToLandingPage) //TODO has to be NavigateUp plus Invalidation
            } else _state.update { it.copy(errorMessage = error.message) }
        }
    }

    private fun onInsertGoalError(error: Throwable?) {
        _state.update { it.copy(errorMessage = error?.message) }
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

    private fun navigateToLandingPage() {
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