package com.mind.market.aimissioncompose.presentation.landing_page.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.presentation.utils.Converters.getGenreIcon
import com.mind.market.aimissioncompose.presentation.utils.Converters.getPriorityIcon
import com.mind.market.aimissioncompose.presentation.utils.Converters.getStatusIcon

@Composable
fun Goal(
    modifier: Modifier = Modifier,
    goal: Goal = Goal.EMPTY,
    onDeleteClicked: (goal: Goal) -> Unit,
    onStatusChangeClicked: (goal: Goal) -> Unit,
) {
    Card( // TODO MIC add click listener to whole Card for detail view..
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.LightGray,
        modifier = Modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Column {
                    Text(
                        text = goal.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${goal.creationDate}",
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onDeleteClicked(goal) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.goal_text_delete_goal)
                    )

                }
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = getGenreIcon(goal.genre)),
                    contentDescription = stringResource(R.string.goal_text_genre),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .height(24.dp)
                        .width(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.LightGray),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = goal.description,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                if (goal.priority != Priority.MEDIUM && goal.priority != Priority.UNKNOWN) {
                    Image(
                        painter = painterResource(id = getPriorityIcon(goal.priority)),
                        contentDescription = stringResource(R.string.goal_text_priority),
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .height(36.dp)
                            .width(36.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { onStatusChangeClicked(goal) }
                ) {
                    Image(
                        painter = painterResource(id = getStatusIcon(goal.status)),
                        contentDescription = stringResource(R.string.goal_text_status),
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .height(36.dp)
                            .width(36.dp)
                    )
                }
            }
        }
    }
}
