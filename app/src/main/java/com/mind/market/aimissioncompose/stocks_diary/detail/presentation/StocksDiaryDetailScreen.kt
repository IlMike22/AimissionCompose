package com.mind.market.aimissioncompose.stocks_diary.detail.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.presentation.common.Chip
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.components.ExpandableSection
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.components.StocksTradingScreen

@Composable
fun StocksDiaryDetailScreen(
    state: StocksDiaryDetailState,
    navController: NavController,
    onEvent: (StocksDiaryDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
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
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = state.stocksDiary.title,
            onValueChange = {
                onEvent(StocksDiaryDetailEvent.OnTitleChanged(it))
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
            value = state.stocksDiary.description,
            maxLines = 5,
            onValueChange = { description ->
                onEvent(StocksDiaryDetailEvent.OnDescriptionChanged(description))
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
                isSelected = state.stocksDiary.mood == Mood.GOOD,
                onSelectionChanged = { newMood ->
                    onEvent(StocksDiaryDetailEvent.OnMoodChanged(Mood.GOOD))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Chip(
                name = "Was okay",
                isSelected = state.stocksDiary.mood == Mood.OKAY,
                onSelectionChanged = {
                    onEvent(StocksDiaryDetailEvent.OnMoodChanged(Mood.OKAY))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Chip(
                name = "Day to forget",
                isSelected = state.stocksDiary.mood == Mood.BAD,
                onSelectionChanged = {
                    onEvent(StocksDiaryDetailEvent.OnMoodChanged(Mood.BAD))
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        ExpandableSection(title = "Bought / sold any Stocks?") {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                StocksTradingScreen(state = state, onEvent = onEvent)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onEvent(
                    StocksDiaryDetailEvent.OnSaveButtonClicked(
                        StocksDiaryDomain(
                            title = state.stocksDiary.title,
                            description = state.stocksDiary.description,
                            mood = state.stocksDiary.mood,
//                            stocksSold = StocksInformation(name = stocksSoldName),
//                            stocksBought = StocksInformation(name = stocksBoughtName)
                        )
                    )
                )
            }) {
            if (!state.isLoading) {
                Text(text = stringResource(R.string.stocks_diary_detail_save_text))
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