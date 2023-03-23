package com.mind.market.aimissioncompose.auth.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.auth.domain.CreateUserUseCase
import com.mind.market.aimissioncompose.auth.domain.LoginUserUseCase
import com.mind.market.aimissioncompose.auth.domain.StoreLocalUserUseCase
import com.mind.market.aimissioncompose.auth.utils.AuthenticationValidationErrorStatus
import com.mind.market.aimissioncompose.presentation.landing_page.ICommandReceiver
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
    private val _state = MutableStateFlow(AuthenticationUiState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000), AuthenticationUiState()
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
                    validationErrorStatus = null,
                    email = event.email
                )
            }
            is AuthenticationEvent.OnPasswordChanged -> _state.update {
                it.copy(
                    validationErrorStatus = null,
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
                val errorCode = validateLogin()
                if (errorCode == null) {
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
                                        isUserAuthenticated = true,
                                        validationErrorStatus = null
                                    )
                                }
                                // TODO MIC nested coroutine bad. Look for solution to run coroutines sequentially
                                viewModelScope.launch {
                                    _uiEvent.send(AuthenticationUiEvent.NavigateToLandingPageAfterLogin)
                                    storeLocalUser(_state.value.user)
                                }
                            } else {
                                // ERROR CASE - show in ui
                                _state.update {
                                    it.copy(
                                        toastMessage = error?.message
                                            ?: "Unknown error while trying to login user.",
                                        isLoading = false,
                                        isUserAuthenticated = false,
                                        validationErrorStatus = AuthenticationValidationErrorStatus.LOGIN_FAILED
                                    )
                                }
                            }
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isUserAuthenticated = false,
                            validationErrorStatus = errorCode
                        )
                    }
                }
            }
            AuthenticationEvent.OnClearEmailText -> _state.update { it.copy(email = "") }
        }
    }

    private fun validateLogin(): AuthenticationValidationErrorStatus? {
        return if (_state.value.email.isBlank()) {
            AuthenticationValidationErrorStatus.NO_EMAIL
        } else if (!Patterns.EMAIL_ADDRESS.matcher(_state.value.email).matches()) {
            AuthenticationValidationErrorStatus.INVALID_EMAIL
        } else if (_state.value.password.isBlank()) {
            AuthenticationValidationErrorStatus.NO_PASSWORD
        } else {
            null
        }
    }
}