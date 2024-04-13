package com.mind.market.aimissioncompose.stocks_diary.detail.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StockTradingDetail
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.StocksDiaryDetailEvent
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.StocksDiaryDetailState

@Composable
fun StocksTradingScreen(
    modifier: Modifier = Modifier,
    state: StocksDiaryDetailState,
    onEvent: (StocksDiaryDetailEvent) -> Unit
) {

    var name by rememberSaveable {
        mutableStateOf("")
    }

    var amount by rememberSaveable {
        mutableStateOf(0f)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = name, //stocksSoldName,
            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
            label = { Text(text = stringResource(id = R.string.stocks_diary_detail_name_text)) },
            placeholder = { Text(text = stringResource(id = R.string.stocks_diary_detail_name_text)) },
            onValueChange = { newName ->
                name = newName
            },
            textStyle = MaterialTheme.typography.body1,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amount.toString(), //stocksSoldName,
            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
            label = { Text(text = stringResource(id = R.string.stocks_diary_detail_amount_text)) },
            placeholder = { Text(text = stringResource(id = R.string.stocks_diary_detail_amount_text)) },
            onValueChange = { newName ->
                amount = newName.toFloat() // TODO MIC check before setting -> validation!
            },
            textStyle = MaterialTheme.typography.body1,
            maxLines = 1
        )
        Button(onClick = {
            onEvent(StocksDiaryDetailEvent.OnStockTradingChange(StockTradingDetail(name = name)))
            name = ""
        }) {
            Text(text = "Add")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column {
            state.stocksDiary.stocksTraded.forEach {
                Box {
                    Text(text = it.name, color = Color.Black)
                }
            }
        }
    }
}

