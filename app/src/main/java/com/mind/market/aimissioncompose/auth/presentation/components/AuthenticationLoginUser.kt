package com.mind.market.aimissioncompose.auth.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationEvent
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationState
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationUiEvent
import com.mind.market.aimissioncompose.navigation.Route
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AuthenticationLoginUser(
    modifier: Modifier = Modifier,
    state: AuthenticationState,
    navController: NavController,
    onEvent: (AuthenticationEvent) -> Unit
) {

    val scaffoldState = rememberScaffoldState()

    if (state.toastMessage != null) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(state.toastMessage)
        }
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.authentication_login_header),
                style = TextStyle.Default
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = stringResource(R.string.authentication_login_email_text))
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = state.email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onValueChange = {
                    onEvent(AuthenticationEvent.OnEmailChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(R.string.authentication_login_password_text))
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.password,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onValueChange = {
                    onEvent(AuthenticationEvent.OnPasswordChanged(it))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onEvent(AuthenticationEvent.OnLoginUser)
                },
                modifier = Modifier
                    .height(82.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(16.dp)
                            .fillMaxSize(),
                        color = MaterialTheme.colors.primary,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(text = stringResource(R.string.authentication_login_button_text))
                }
            }
        }
    }
}