package com.mind.market.aimissioncompose.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mind.market.aimissioncompose.R


@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state = viewModel.state
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.detail_goal_title),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.width(24.dp))
            TextField(
                value = state.goal.title,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { newTitle ->
                    viewModel.state = viewModel.state.copy(
                        goal = state.goal.copy(
                            title = newTitle
                        )
                    )
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.detail_goal_description),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )
            Spacer(Modifier.width(24.dp))
            TextField(
                value = state.goal.description,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { newDescription ->
                    viewModel.state = viewModel.state.copy(
                        goal = state.goal.copy(
                            description = newDescription
                        )
                    )
                }
            )
        }
    }
}