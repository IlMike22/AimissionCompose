package com.mind.market.aimissioncompose.statistics.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity

@Composable
fun StatisticsItem(
    item: StatisticsEntity,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            Text(
                text = item.title,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontStyle = FontStyle.Normal
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Goals created: ${item.amountGoalsCreated}",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Goals completed: ${item.amountGoalsCompleted}",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Your grade: ${item.grade}",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontStyle = FontStyle.Normal
            )
        }
    }
}