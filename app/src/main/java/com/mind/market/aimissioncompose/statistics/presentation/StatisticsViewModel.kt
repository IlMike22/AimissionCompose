package com.mind.market.aimissioncompose.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.GenerateStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val generateStatistics: GenerateStatisticsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(StatisticsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), StatisticsState()
    )

    private var auth: FirebaseAuth = Firebase.auth

    init {
        viewModelScope.launch {
            generateStatistics()
                .collectLatest { entities ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            statisticsEntities = entities,
                            isUsersAuthenticated = auth.currentUser != null
                        )
                    }
                }
        }
    }

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            StatisticsEvent.OnCloseStatistics -> TODO()
            is StatisticsEvent.OnSortModeChanged -> Unit
        }
    }
}