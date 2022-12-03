package com.mind.market.aimissioncompose.presentation.landing_page.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val repository: IGoalRepository,
) : ViewModel() {
    private var _goalUiEvent = Channel<GoalUiEvent>()
    val goalUiEvent = _goalUiEvent.receiveAsFlow()

    var goalState by mutableStateOf(GoalState())
        private set

    fun onEvent(event: GoalEvent) {
        when (event) {
            is GoalEvent.OnGoalUiStatusChanged -> {
                viewModelScope.launch {
                    val newIcon = when (event.newStatus) {
                        Status.TODO -> {
                            Icons.Default.Create
                        }
                        Status.IN_PROGRESS -> {
                            Icons.Default.Build
                        }
                        Status.DONE -> {
                            Icons.Default.Done
                        }
                        else -> {
                            Icons.Default.Warning
                        }
                    }
                    _goalUiEvent.send(GoalUiEvent.ChangeStatusIcon(newIcon))
                    goalState = goalState.copy(
                        statusIcon = newIcon
                    )
                }
            }
        }
    }

    private fun changeUiStatus(newStatus: Status): ImageVector {
        return when (newStatus) {
            Status.TODO -> Icons.Default.Build
            Status.IN_PROGRESS -> Icons.Default.Done
            Status.DONE -> Icons.Default.Create
            else -> Icons.Default.Close
        }
    }
}