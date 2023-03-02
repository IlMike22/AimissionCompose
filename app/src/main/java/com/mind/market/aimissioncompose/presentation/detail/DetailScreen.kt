package com.mind.market.aimissioncompose.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.getGenres
import com.mind.market.aimissioncompose.domain.models.getPriorities
import com.mind.market.aimissioncompose.presentation.common.ChipGroupGenre
import com.mind.market.aimissioncompose.presentation.common.ChipGroupPriority
import com.mind.market.aimissioncompose.presentation.common.MainButton
import com.mind.market.aimissioncompose.presentation.utils.Converters.getGenreIcon
import com.mind.market.aimissioncompose.presentation.utils.Converters.getPriorityIcon
import com.mind.market.aimissioncompose.presentation.utils.Converters.toGenre
import com.mind.market.aimissioncompose.presentation.utils.Converters.toPriority
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state // TODO MIC go on here next time passing state without vm to screen as well as onEvent function
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val dateDialogState = rememberMaterialDialogState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is DetailUIEvent.NavigateToLandingPage -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle?.set("invalidate", true)
                    navController.popBackStack()
                }
            }
        }
    }

    LazyColumn() {
        item {
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
                            viewModel.onEvent(DetailEvent.OnTitleChanged(newTitle))
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
                            viewModel.onEvent(DetailEvent.OnDescriptionChanged(newDescription))
                        }
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.detail_goal_priority),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (state.goal.priority != Priority.UNKNOWN && state.goal.priority != Priority.MEDIUM) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Image(
                                painter = painterResource(
                                    id = getPriorityIcon(state.goal.priority)
                                ),
                                contentDescription = "Genre"
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ChipGroupPriority(
                            values = getPriorities(),
                            selectedValue = state.goal.priority,
                            onSelectedChanged = { newPriority ->
                                viewModel.onEvent(DetailEvent.OnPriorityChanged(newPriority.toPriority()))
                            }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.detail_goal_genre),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Image(
                            painter = painterResource(
                                id = getGenreIcon(state.goal.genre)
                            ),
                            contentDescription = "Genre"
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ChipGroupGenre(
                            values = getGenres(),
                            selectedValue = state.goal.genre,
                            onSelectedChanged = { newGenre ->
                                viewModel.onEvent(DetailEvent.OnGenreChanged(newGenre.toGenre()))
                            }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.detail_goal_finish_date),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(onClick = {
                            dateDialogState.show()
                        }) {
                            Text(text = "Pick a date")
                        }
                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = DateTimeFormatter
                                .ofPattern("dd.MM.yyyy")
                                .format(state.goal.finishDate)
                        )
                    }
                }

                MainButton(
                    text = state.ctaButtonText,
                    icon = 0,
                    onClick = { viewModel.onEvent(DetailEvent.OnSaveButtonClicked) }
                )
            }
        }
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "OK")
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date"
        ) { picked ->
            pickedDate = picked
            viewModel.onEvent(DetailEvent.OnFinishDateChanged(picked))
        }
    }
}