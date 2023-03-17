package com.mind.market.aimissioncompose.presentation.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.domain.information.use_case.GetInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val getInformation: GetInformationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InformationState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        InformationState()
    )

    init {
        viewModelScope.launch {
            val information = getInformation()
            _state.update {
                it.copy(
                    information = information
                )
            }
        }
    }

    fun onEvent(event: InformationEvent) {
        when (event) {
            InformationEvent.OnChangeVersion -> changeVersion()
        }
    }

    private fun changeVersion() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    information = listOf(
                        "IlMike22",
                        "1.0"
                    )
                )
            }
        }
    }
}