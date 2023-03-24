package com.mind.market.aimissioncompose.auth.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationEvent
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationUiState
import com.mind.market.aimissioncompose.auth.utils.toText
import com.mind.market.aimissioncompose.presentation.common.ClearIcon

@Composable
fun AuthenticationCreateUser(
    modifier: Modifier = Modifier,
    state: AuthenticationUiState,
    onEvent: (AuthenticationEvent) -> Unit
) {
    val context = LocalContext.current
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordHidden by remember { mutableStateOf(false) }
    val icon =
        if (isPasswordHidden) painterResource(id = com.google.android.material.R.drawable.design_ic_visibility)
        else painterResource(id = com.google.android.material.R.drawable.design_ic_visibility_off)

    Text(
        text = "Create a new account to add your first goal.",
        style = MaterialTheme.typography.body1,
        color = Color.Black,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(24.dp))
    OutlinedTextField(
        value = state.email,
        textStyle = TextStyle(color = Color.Black),
        placeholder = { Text(text = stringResource(R.string.authentication_login_placeholder_email)) },
        label = { Text(text = stringResource(R.string.authentication_login_placeholder_email)) },
        trailingIcon = {
            if (state.email.isNotBlank()) ClearIcon(onEvent::invoke)
        },
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
        textStyle = TextStyle(color = Color.Black),
        placeholder = { Text(text = stringResource(R.string.authentication_login_placeholder_password)) },
        label = { Text(text = stringResource(R.string.authentication_login_placeholder_password)) },
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
    Spacer(modifier = Modifier.height(32.dp))
    if (state.validationErrorStatus != null) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = state.validationErrorStatus.toText(context),
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    Button(
        onClick = { onEvent(AuthenticationEvent.OnCreateNewUser) }
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(16.dp),
                color = MaterialTheme.colors.secondary,
                strokeWidth = 3.dp
            )
        } else {
            Text(text = "Create new account")
        }
    }
}