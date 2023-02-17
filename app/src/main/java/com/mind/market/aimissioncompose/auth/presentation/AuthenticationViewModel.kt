package com.mind.market.aimissioncompose.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.auth.domain.CreateUser
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val createUser: CreateUser
) : ViewModel() {

    private val _state = MutableStateFlow(AuthenticationState())
    var state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000), AuthenticationState()
    )

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.OnCreateNewUser -> {
                viewModelScope.launch {
                    createUser(_state.value.email, _state.value.password)
                }
            }
            is AuthenticationEvent.OnEmailChanged -> _state.update {
                it.copy(
                    email = event.email
                )
            }
            is AuthenticationEvent.OnPasswordChanged -> _state.update {
                it.copy(
                    password = event.password
                )
            }
        }
    }
}