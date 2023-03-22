package com.mind.market.aimissioncompose.statistics.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsGrade
import com.mind.market.aimissioncompose.ui.theme.DarkerBlue
import com.mind.market.aimissioncompose.ui.theme.DarkestBlue

@Composable
fun StatisticsItem(
    item: StatisticsEntity,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                style = MaterialTheme.typography.h5,
                color = DarkestBlue
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.TopStart),
                    text = stringResource(R.string.statistics_item_text_goals_created),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                    color = DarkerBlue
                )
                Text(
                    modifier = Modifier.align(Alignment.TopEnd),
                    text = "${item.data.totalAmount}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.body1,
                    color = DarkerBlue,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.TopStart),
                    text = stringResource(R.string.statistics_item_text_goals_started),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                    color = DarkerBlue
                )
                Text(
                    modifier = Modifier.align(Alignment.TopEnd),
                    text = "${item.data.totalGoalsInProgress}",
                    style = MaterialTheme.typography.body1,
                    color = DarkerBlue,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.statistics_item_text_goals_completed),
                    modifier = Modifier.align(Alignment.TopStart),
                    style = MaterialTheme.typography.body1,
                    color = DarkerBlue
                )
                Text(
                    modifier = Modifier.align(Alignment.TopEnd),
                    text = "${item.data.totalGoalsCompleted}",
                    style = MaterialTheme.typography.body1,
                    color = DarkerBlue,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.statistics_item_text_goals_deprecated),
                    modifier = Modifier.align(Alignment.TopStart),
                    style = MaterialTheme.typography.body1,
                    color = DarkerBlue
                )
                Text(
                    modifier = Modifier.align(Alignment.TopEnd),
                    text = "${item.data.totalGoalsDeprecated}",
                    style = MaterialTheme.typography.body1,
                    color = if (item.data.totalGoalsDeprecated > 0) Color.Red else Color.Green,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = item.grade.toText(context),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                color = DarkerBlue
            )
        }
    }
}

fun StatisticsGrade.toText(context: Context): String =
    when (this) {
        StatisticsGrade.ALL_GOALS_COMPLETED -> context.getString(R.string.statistics_item_grade_text_all_goals_completed)
        StatisticsGrade.SOME_GOALS_COMPLETED -> context.getString(R.string.statistics_item_grade_text_some_goals_completed)
        StatisticsGrade.NO_GOALS_COMPLETED -> context.getString(R.string.statistics_item_grade_text_no_goals_completed)
        StatisticsGrade.NO_GOALS_ADDED -> context.getString(R.string.statistics_item_grade_text_no_goals_added)
        StatisticsGrade.DEPRECATED_GOAL_EXIST -> context.getString(R.string.statistics_item_grade_text_deprecated_goal)
        StatisticsGrade.UNDEFINED -> context.getString(R.string.statistics_item_grade_text_unknown_state)
    }