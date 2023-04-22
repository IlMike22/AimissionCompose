package com.mind.market.aimissioncompose.presentation.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun CustomMultilineHintTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier,
    hint: String = "",
    textStyle: TextStyle = MaterialTheme.typography.body1,
    maxLines: Int = 4
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = textStyle,
        maxLines = maxLines,
        decorationBox = { innerTextField ->
            Box(modifier = modifier) {
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium) // make it a lil bit transparent
                    )
                }
                innerTextField
            }
        }
    )
}