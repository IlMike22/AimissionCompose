package com.mind.market.aimissioncompose.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.getPriorities

@Composable
fun ChipGroup(
    values: List<Priority> = getPriorities(), // TODO move out this Priority detail...
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