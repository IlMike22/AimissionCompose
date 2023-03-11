package com.mind.market.aimissioncompose.statistics.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mind.market.aimissioncompose.statistics.data.dto.Grade
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity

@Composable
fun StatisticsItem(
    item: StatisticsEntity,
    grade: Grade,
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
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.TopStart),
                    text = "Goals created:",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    modifier = Modifier.align(Alignment.TopEnd),
                    text = "${item.amountGoalsCreated}",
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.TopStart),
                    text = "Goals started:",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    modifier = Modifier.align(Alignment.TopEnd),
                    text = "${item.amountGoalsStarted}",
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Goals completed:",
                    modifier = Modifier.align(Alignment.TopStart),
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    modifier = Modifier.align(Alignment.TopEnd),
                    text = "${item.amountGoalsCompleted}",
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Your grade: $grade",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontStyle = FontStyle.Normal
            )
        }
    }
}