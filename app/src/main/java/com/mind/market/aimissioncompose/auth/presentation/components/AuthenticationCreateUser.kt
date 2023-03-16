package com.mind.market.aimissioncompose.auth.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationEvent
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationUiState

@Composable
fun AuthenticationCreateUser(
    modifier: Modifier = Modifier,
    state: AuthenticationUiState,
    onEvent: (AuthenticationEvent) -> Unit
) {
    Text(text = "Or register a new account if you want", style = TextStyle.Default)
    Spacer(modifier = Modifier.height(24.dp))
    Text(text = "Type email here:")
    Spacer(modifier = Modifier.height(24.dp))
    TextField(value = state.email, onValueChange = {
        onEvent(AuthenticationEvent.OnEmailChanged(it))
    })

    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Type password here:")
    Spacer(modifier = Modifier.height(24.dp))

    TextField(value = state.password, onValueChange = {
        onEvent(AuthenticationEvent.OnPasswordChanged(it))
    })

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = { onEvent(AuthenticationEvent.OnCreateNewUser) }
    ) {
        Text(text = "Register new user")
    }
}