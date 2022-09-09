package com.mind.market.aimissioncompose.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.domain.models.*
import com.mind.market.aimissioncompose.presentation.common.ChipGroupGenre
import com.mind.market.aimissioncompose.presentation.common.ChipGroupPriority
import com.mind.market.aimissioncompose.presentation.common.MainButton
import kotlinx.coroutines.flow.collect


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    onNavigateToLandingPage: () -> Unit
) {
    val state = viewModel.state
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is DetailUIEvent.NavigateToLandingPage -> {
                    onNavigateToLandingPage()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.detail_goal_description),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
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
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.detail_goal_priority),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ChipGroupPriority(
                    values = getPriorities(),
                    selectedValue = state.goal.priority,
                    onSelectedChanged = {
                        viewModel.state = state.copy(
                            goal = state.goal.copy(
                                priority = getPriority(it) ?: Priority.UNKNOWN
                            )
                        )
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.detail_goal_genre),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ChipGroupGenre(
                    values = getGenres(),
                    selectedValue = state.goal.genre,
                    onSelectedChanged = {
                        viewModel.state = state.copy(
                            goal = state.goal.copy(
                                genre = getGenre(it) ?: Genre.UNKNOWN
                            )
                        )
                    }
                )
            }
        }


        MainButton(
            text = stringResource(id = R.string.button_add),
            icon = 0,
            onClick = {
                viewModel.onSaveGoalButtonClicked()
            })
    }
}