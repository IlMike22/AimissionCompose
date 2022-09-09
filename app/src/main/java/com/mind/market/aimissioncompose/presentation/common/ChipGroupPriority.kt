package com.mind.market.aimissioncompose.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.domain.models.*

@Composable
fun ChipGroupPriority(
    values: List<Priority> = getPriorities(),
    selectedValue: Priority? = null,
    onSelectedChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        LazyRow {
            items(values) { value ->
                Chip(name = value.name,
                    isSelected = selectedValue == value,
                    onSelectionChanged = {
                        onSelectedChanged(it)
                    }
                )
            }
        }
    }
}