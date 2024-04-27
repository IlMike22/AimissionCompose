package com.mind.market.aimissioncompose.stocks_diary.detail.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.StocksDiaryDetailEvent
import java.math.BigDecimal

@Composable
fun StocksTradingItemCreateDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onEvent: (StocksDiaryDetailEvent) -> Unit,
) {
    var stocksName by remember {
        mutableStateOf("")
    }

    var amount by remember {
        mutableStateOf(0)
    }

    var reason by remember {
        mutableStateOf("")
    }

    var pricePerStock by remember {
        mutableStateOf(BigDecimal(0))
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = stocksName, //stocksSoldName,
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
                    label = { Text(text = stringResource(id = R.string.stocks_diary_detail_name_text)) },
                    placeholder = { Text(text = stringResource(id = R.string.stocks_diary_detail_name_text)) },
                    onValueChange = { newName ->
                        stocksName = newName
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
                    onValueChange = { newAmount ->
                        amount = try {
                            newAmount.toInt()
                        } catch (ecx:Exception) {
                            0
                        }
                    },
                    textStyle = MaterialTheme.typography.body1,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = pricePerStock.toString(), //stocksSoldName,
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
                    label = { Text(text = stringResource(id = R.string.stocks_diary_detail_price_per_stock_text)) },
                    placeholder = { Text(text = stringResource(id = R.string.stocks_diary_detail_price_per_stock_text)) },
                    onValueChange = { newPricePerStock ->
                        pricePerStock = try {
                            newPricePerStock.toBigDecimal()
                        } catch(exc:Exception) {
                            BigDecimal(0)
                        }
                    },
                    textStyle = MaterialTheme.typography.body1,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                           onDismiss()
                        }) {
                        Text(text = "Dismiss")
                    }
                    Spacer(Modifier.width(16.dp))
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            onEvent(
                                StocksDiaryDetailEvent.OnAddNewTradingItem(
                                    name = stocksName,
                                    amount = amount,
                                    reason = reason,
                                    pricePerStock = pricePerStock
                                )
                            )
                            onDismiss()
                        }) {
                        Text(text = "Add")
                    }
                }
            }
        }
    }
}