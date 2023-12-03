package com.mind.market.aimissioncompose.stocks_diary.detail.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.presentation.common.Chip
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

@Composable
fun StocksDiaryDetailScreen(
    state: StocksDiaryDetailState,
    navController: NavController,
    onEvent: (StocksDiaryDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var moodSelection by remember {
        mutableStateOf(Mood.GOOD)
    }

    LaunchedEffect(key1 = state.isNavigateBack) {
        if (state.isNavigateBack) {
            navController.previousBackStackEntry
                ?.savedStateHandle?.set("invalidate", true)
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = title,
            onValueChange = {
                title = it
            },
            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
            textStyle = MaterialTheme.typography.body1,
            label = {
                Text(text = "Enter a good headline.")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = null)
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    println("User clicked next on the keyboard")
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(8.dp),
            value = description,
            maxLines = 5,
            onValueChange = {
                description = it
            },
            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
            textStyle = MaterialTheme.typography.body1,
            label = {
                Text(text = "Enter a good description.")
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    println("User clicked next on the keyboard")
                }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Chip(
                name = "Good day",
                isSelected = moodSelection == Mood.GOOD,
                onSelectionChanged = {
                    moodSelection = Mood.GOOD
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Chip(
                name = "Was okay",
                isSelected = moodSelection == Mood.OKAY,
                onSelectionChanged = {
                    moodSelection = Mood.OKAY
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Chip(
                name = "Day to forget",
                isSelected = moodSelection == Mood.BAD,
                onSelectionChanged = {
                    moodSelection = Mood.BAD
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onEvent(
                    StocksDiaryDetailEvent.OnSaveButtonClicked(
                        StocksDiaryDomain(
                            title = title,
                            description = description,
                            mood = moodSelection
                        )
                    )
                )
            }) {
            if (!state.isLoading) {
                Text(text = "Save")
            } else {
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = Color.Red
            )
        }
    }
}