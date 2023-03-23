package com.mind.market.aimissioncompose.presentation.landing_page.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.presentation.utils.Converters.getStatusIcon
import com.mind.market.aimissioncompose.presentation.utils.Converters.toText
import com.mind.market.aimissioncompose.ui.theme.LighterBlue
import java.time.LocalDateTime

@Composable
fun Goal(
    modifier: Modifier = Modifier,
    goal: Goal = Goal.EMPTY,
    onClicked: (Goal) -> Unit,
    onDeleteClicked: (Goal) -> Unit,
    onStatusChangeClicked: (Goal) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = LighterBlue,
        modifier = Modifier
            .clickable { onClicked(goal) }
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
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = goal.title,
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Created date: ${goal.creationDate.toText()}",
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.caption
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Finish date: ${goal.finishDate.toText()}",
                        color = if (goal.finishDate <= LocalDateTime.now() && goal.status != Status.DONE) Color.Red else Color.DarkGray,
                        style = MaterialTheme.typography.caption
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = { onDeleteClicked(goal) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.goal_text_delete_goal),
                        tint = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = goal.description,
                    style = MaterialTheme.typography.body2,
                    color = Color.DarkGray,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                if (goal.priority != Priority.MEDIUM && goal.priority != Priority.UNKNOWN) {
                    Tag(
                        type = TagType.PRIORITY_TAG,
                        text = goal.priority.toText()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Tag(
                    type = TagType.GENRE_TAG,
                    text = goal.genre.toText()
                )

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

@Composable
fun Tag(
    type: TagType,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        TextButton(
            onClick = {},
            enabled = false,
            colors = if (type == TagType.PRIORITY_TAG) ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground,
                disabledContentColor = MaterialTheme.colors.onBackground
            ) else ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary,
                disabledContentColor = MaterialTheme.colors.onSecondary
            )

        ) {
            Text(
                text = text,
                color = Color.DarkGray,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

enum class TagType {
    PRIORITY_TAG,
    GENRE_TAG
}

fun Priority.toText(): String =
    when (this) {
        Priority.LOW -> "low priority"
        Priority.MEDIUM -> ""
        Priority.HIGH -> "high priority"
        Priority.UNKNOWN -> ""
    }

fun Genre.toText(): String =
    when (this) {
        Genre.BUSINESS -> "business"
        Genre.FITNESS -> "fitness"
        Genre.MONEY -> "money"
        Genre.PARTNERSHIP -> "partnership"
        Genre.SOCIALISING -> "socialising"
        Genre.HEALTH -> "health"
        Genre.UNKNOWN -> ""
    }
