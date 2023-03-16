package com.mind.market.aimissioncompose.auth.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationEvent
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AuthenticationLoginUser(
    modifier: Modifier = Modifier,
    state: AuthenticationState,
    navController: NavController,
    onEvent: (AuthenticationEvent) -> Unit
) {

    val scaffoldState = rememberScaffoldState()

    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordHidden by remember { mutableStateOf(false) }

    val icon =
        if (isPasswordHidden) painterResource(id = com.google.android.material.R.drawable.design_ic_visibility)
        else painterResource(id = com.google.android.material.R.drawable.design_ic_visibility_off)

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
                style = TextStyle.Default,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = state.email,
                placeholder = { Text(text = "Email Address") },
                label = { Text(text = "Your email address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onValueChange = {
                    onEvent(AuthenticationEvent.OnEmailChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.password,
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Your password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onValueChange = {
                    onEvent(AuthenticationEvent.OnPasswordChanged(it))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { isPasswordHidden = !isPasswordHidden }) {
                        Icon(painter = icon, contentDescription = "visibility icon")
                    }
                },
                visualTransformation = if (isPasswordHidden) VisualTransformation.None else PasswordVisualTransformation()
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
                            .size(16.dp),
                        color = MaterialTheme.colors.secondary,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(text = stringResource(R.string.authentication_login_button_text))
                }
            }
        }
    }
}