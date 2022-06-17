package com.mind.market.aimissioncompose.presentation.information

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aimissionlite.domain.information.use_case.IInformationUseCase
import com.mind.market.aimissioncompose.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val useCase: IInformationUseCase
) : ViewModel() {

    val information = MutableLiveData<Resource<Map<String, String>>>()

    init {
        setInformation()
    }

    private fun setInformation() {
        information.postValue(Resource.Loading())
        information.postValue(
            Resource.Success(
                data = useCase.getInformation()
            )
        )
    }
}