package com.mind.market.aimissioncompose.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.auth.domain.CreateUserUseCase
import com.mind.market.aimissioncompose.auth.domain.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val createUser: CreateUserUseCase,
    private val loginUser: LoginUserUseCase
) : ViewModel() {
    private val TAG = "AuthenticationViewModel"
    private val _state = MutableStateFlow(AuthenticationState())
    val state = _state.stateIn(
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
            AuthenticationEvent.OnLoginUser -> {
                val resultCode = validateLogin()
                if (resultCode == ValidationCode.OK) {
                    viewModelScope.launch {
                        loginUser(_state.value.email, _state.value.password)
                    }
                } else {
                    Log.e(TAG, "Cannot login user. Email or password are invalid or empty.")
                }
            }
        }
    }

    private fun validateLogin(): ValidationCode {
        return if (_state.value.email.isBlank() || _state.value.password.isBlank()) {
            ValidationCode.MISSING_EMAIL_OR_PASSWORD
        } else {
            ValidationCode.OK
        }
    }

    enum class ValidationCode {
        OK,
        MISSING_EMAIL_OR_PASSWORD
    }
}