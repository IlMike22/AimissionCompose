package com.mind.market.aimissioncompose.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mind.market.aimissioncompose.auth.domain.CreateUserUseCase
import com.mind.market.aimissioncompose.auth.domain.LoginUserUseCase
import com.mind.market.aimissioncompose.auth.domain.StoreLocalUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val createUser: CreateUserUseCase,
    private val storeLocalUser: StoreLocalUserUseCase,
    private val loginUser: LoginUserUseCase
) : ViewModel() {
    private val TAG = "AuthenticationViewModel"
    private val _state = MutableStateFlow(AuthenticationState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000), AuthenticationState()
    )

    private val _uiEvent = Channel<AuthenticationUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
                _state.update {
                    it.copy(
                        toastMessage = null,
                        isLoading = true
                    )
                }
                val resultCode = validateLogin()
                if (resultCode == ValidationCode.OK) {
                    viewModelScope.launch {
                        loginUser(
                            _state.value.email,
                            _state.value.password
                        ) { user, error ->
                            if (error == null && user != null) { // SUCCESS CASE
                                // success case
                                _state.update {
                                    it.copy(
                                        user = user,
                                        toastMessage = "User ${user.id} successfully logged in.",
                                        isLoading = false,
                                        isUserAuthenticated = true
                                    )
                                }
                                // TODO MIC nested coroutine bad. Look for solution to run coroutines sequentially
                                viewModelScope.launch {
                                    _uiEvent.send(AuthenticationUiEvent.NavigateToLandingPageAfterLogin)
                                    storeLocalUser(_state.value.user)
                                }
                            } else {
                                // error case - show in ui
                                _state.update {
                                    it.copy(
                                        toastMessage = error?.message
                                            ?: "Unknown error while trying to login user.",
                                        isLoading = false,
                                        isUserAuthenticated = false
                                    )
                                }
                            }
                        }
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