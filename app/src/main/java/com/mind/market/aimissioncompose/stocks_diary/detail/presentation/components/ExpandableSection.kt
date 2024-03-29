package com.mind.market.aimissioncompose.stocks_diary.detail.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableSectionTitle(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    title:String
) {
    val icon = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            imageVector = icon,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onPrimary),
            contentDescription = null
        )

        Text(text = title, style = MaterialTheme.typography.h6)
    }
}

@Composable
fun ExpandableSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(color = MaterialTheme.colors.primary)
            .fillMaxWidth()
    ) {
        ExpandableSectionTitle(isExpanded = isExpanded, title = title)

        AnimatedVisibility(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxWidth(),
            visible = isExpanded
        ) {
            content()
        }
    }

}