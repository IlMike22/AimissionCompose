package com.mind.market.aimissioncompose.presentation.landing_page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mind.market.aimissioncompose.domain.models.Goal

@Composable
fun Goal(
    modifier: Modifier = Modifier,
    goal: Goal,
    onDeleteClicked: () -> Unit,
    onStatusChangeClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = goal.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = goal.description,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = onDeleteClicked
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete goal"
            )

        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = onStatusChangeClicked
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Delete goal"
            )

        }
    }
}