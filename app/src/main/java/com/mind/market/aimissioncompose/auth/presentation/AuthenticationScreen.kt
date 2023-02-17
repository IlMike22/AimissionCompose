package com.mind.market.aimissioncompose.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthenticationScreen(
    modifier: Modifier = Modifier,
    state: AuthenticationState,
    onEvent: (AuthenticationEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Type email here:")
            Spacer(modifier = Modifier.width(24.dp))
            TextField(value = state.email, onValueChange = {
                onEvent(AuthenticationEvent.OnEmailChanged(it))
            })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Type password here:")
            Spacer(modifier = Modifier.width(24.dp))
            TextField(value = state.password, onValueChange = {
                onEvent(AuthenticationEvent.OnPasswordChanged(it))
            })
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onEvent(AuthenticationEvent.OnCreateNewUser) }
        ) {
            Text(text = "Register new user")
        }
    }
}