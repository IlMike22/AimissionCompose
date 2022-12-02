package com.mind.market.aimissioncompose.presentation.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.information.use_case.IInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val useCase: IInformationUseCase
) : ViewModel() {

    private val _informationFlow =
        MutableStateFlow<Resource<List<String>>>(Resource.Loading(true))
    val informationFlow = _informationFlow as StateFlow<Resource<List<String>>>

    init {
        setInformation()
    }

    fun changeVersion() {
        viewModelScope.launch {
            _informationFlow.emit(Resource.Loading(true))
            delay(2000)
            _informationFlow.emit(
                Resource.Success(
                    listOf(
                        "IlMike22",
                        "1.0"
                    )
                )
            )
        }
    }

    private fun setInformation() {
        viewModelScope.launch {
            _informationFlow.emit(
                Resource.Success(
                    data = useCase.getInformation()
                )
            )
        }
    }
}