package com.mind.market.aimissioncompose.auth.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
    TextField(
        value = state.email,
        onValueChange = { onEvent(AuthenticationEvent.OnEmailChanged(it)) }
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(text = stringResource(R.string.authentication_login_password_text))
    Spacer(modifier = Modifier.height(24.dp))

    TextField(
        value = state.password,
        onValueChange = { onEvent(AuthenticationEvent.OnPasswordChanged(it)) }
    )
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = { onEvent(AuthenticationEvent.OnLoginUser) }
    ) {
        Text(text = stringResource(R.string.authentication_login_button_text))
    }
}