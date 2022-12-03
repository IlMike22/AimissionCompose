package com.mind.market.aimissioncompose.presentation.landing_page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.domain.models.Goal

@Composable
fun Goal(
    modifier: Modifier = Modifier,
    goal: Goal = Goal.EMPTY,
    viewModel: GoalViewModel = hiltViewModel(),
    onDeleteClicked: (goal: Goal) -> Unit,
    navController: NavController
) {
    var statusIcon = viewModel.goalState.statusIcon
//    var statusIcon: ImageVector =
//        if (goal.status == Status.IN_PROGRESS) Icons.Default.Build else Icons.Default.Done

    LaunchedEffect(key1 = true) {
        viewModel.goalUiEvent.collect { event ->
            when (event) {
                is GoalUiEvent.ChangeStatusIcon -> {
                    statusIcon = event.newIcon
                }
            }
        }
    }

    Card(
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
                Text(
                    text = goal.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit goal"
                    )

                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { onDeleteClicked(goal) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete goal"
                    )
                }
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
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Priority",
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        viewModel.onEvent(GoalEvent.OnGoalUiStatusChanged(goal.status))
                    }
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = "Complete"
                    )
                }
            }
        }
    }
}