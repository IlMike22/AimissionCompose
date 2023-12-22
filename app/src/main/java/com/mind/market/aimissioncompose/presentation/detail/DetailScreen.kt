package com.mind.market.aimissioncompose.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.domain.models.getGenres
import com.mind.market.aimissioncompose.domain.models.getPriorities
import com.mind.market.aimissioncompose.presentation.common.ChipGroupGenre
import com.mind.market.aimissioncompose.presentation.common.ChipGroupPriority
import com.mind.market.aimissioncompose.presentation.common.MainButton
import com.mind.market.aimissioncompose.presentation.utils.Converters.getGenreIcon
import com.mind.market.aimissioncompose.presentation.utils.Converters.getPriorityIcon
import com.mind.market.aimissioncompose.presentation.utils.Converters.toGenre
import com.mind.market.aimissioncompose.presentation.utils.Converters.toPriority
import com.mind.market.aimissioncompose.presentation.utils.Converters.toText
import com.mind.market.aimissioncompose.ui.theme.DarkBlue
import com.mind.market.aimissioncompose.ui.theme.DarkestBlue
import com.mind.market.aimissioncompose.ui.theme.LightestBlue
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    state: DetailUiState,
    uiEvent: Flow<DetailUIEvent>,
    onEvent: (DetailEvent) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val dateDialogState = rememberMaterialDialogState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = keyboardController) {
        uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is DetailUIEvent.NavigateToLandingPage -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle?.set("invalidate", true)
                    navController.popBackStack()
                }

                DetailUIEvent.HideKeyboard -> TODO()
                DetailUIEvent.NavigateToInfo -> TODO()
                DetailUIEvent.NavigateToSettings -> TODO()
                is DetailUIEvent.ShowValidationResult -> TODO()
            }
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(16.dp)
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    } else if (!state.isUserAuthenticated) {
        UserNotAuthenticatedScreen(modifier)
    } else {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            backgroundColor = LightestBlue,
            shape = RoundedCornerShape(8.dp)
        ) {
            LazyColumn {
                item {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        OutlinedTextField(
                            value = state.goal.title,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(color = Color.Black),
                            placeholder = { Text(text = "Goal`s title") },
                            label = { Text(text = "Goal`s title") },
                            maxLines = 1,
                            onValueChange = { newTitle ->
                                onEvent(DetailEvent.OnTitleChanged(newTitle))
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = state.goal.description,
                            textStyle = TextStyle(color = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(256.dp),
                            placeholder = {
                                Text(text = stringResource(R.string.detail_goal_description_hint_text)) },
                            label = {
                                Text(text = stringResource(R.string.detail_goal_description_hint_text)) },
                            onValueChange = { newDescription ->
                                onEvent(DetailEvent.OnDescriptionChanged(newDescription))
                            },
                            maxLines = 5
                        )
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.detail_goal_priority),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.body1
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
                                        onEvent(DetailEvent.OnPriorityChanged(newPriority.toPriority()))
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
                                    color = Color.Black,
                                    style = MaterialTheme.typography.body1
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
                                        onEvent(DetailEvent.OnGenreChanged(newGenre.toGenre()))
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
                                color = Color.Black,
                                style = MaterialTheme.typography.body1
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
                                    color = DarkBlue,
                                    text = DateTimeFormatter
                                        .ofPattern("dd.MM.yyyy")
                                        .format(state.goal.finishDate)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        AnimatedVisibility(visible = state.hasValidationErrors) {
                            Text(
                                text = state.validationCode?.toText(context)
                                    ?: context.getString(R.string.detail_validation_error_message_unknown),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                style = LocalTextStyle.current.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.error
                                )
                            )
                        }
                        if (state.goal.status == Status.DEPRECATED) {
                            Text(
                                text = stringResource(R.string.detail_error_message_goal_is_deprecated),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        MainButton(
                            text = state.ctaButtonText,
                            icon = 0,
                            onClick = { onEvent(DetailEvent.OnSaveButtonClicked) }
                        )
                    }
                }
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
            colors = DatePickerDefaults.colors(
                headerTextColor = LightestBlue,
                dateActiveTextColor = Color.Black,
                dateInactiveTextColor = DarkestBlue
            ),
            title = "Pick a date"
        ) { picked ->
            pickedDate = picked
            onEvent(
                DetailEvent.OnFinishDateChanged(
                    LocalDateTime.of(
                        picked.year,
                        picked.month,
                        picked.dayOfMonth,
                        LocalDateTime.now().hour,
                        LocalDateTime.now().minute,
                        LocalDateTime.now().second
                    )
                )
            )
        }
    }
}

@Composable
fun UserNotAuthenticatedScreen(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier
                    .weight(1f)
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.ic_baseline_sports_handball_yellow_24),
                contentDescription = "not authenticated screen"
            )
            Text(
                text = stringResource(R.string.detail_user_not_authenticated_error_title),
                style = MaterialTheme.typography.h6,
                color = Color.Black
            )
            Text(
                text = stringResource(R.string.detail_user_not_authenticated_error_subtitle),
                modifier = Modifier.padding(bottom = 128.dp),
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
        }
    }

}