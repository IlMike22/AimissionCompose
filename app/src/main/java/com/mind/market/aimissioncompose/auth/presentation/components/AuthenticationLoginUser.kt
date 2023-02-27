package com.mind.market.aimissioncompose.auth.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationEvent
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationState

@Composable
fun AuthenticationLoginUser(
    modifier: Modifier = Modifier,
    state: AuthenticationState,
    onEvent: (AuthenticationEvent) -> Unit
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
        Text(text = stringResource(R.string.authentication_login_button_text))
    }
}