package com.mind.market.aimissioncompose.presentation.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.domain.information.use_case.GetInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val getInformation: GetInformationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InformationState())
    val state = _state.asStateFlow()

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
}