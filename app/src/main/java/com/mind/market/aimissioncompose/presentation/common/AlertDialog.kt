package com.mind.market.aimissioncompose.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    onSuccessClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    title: String,
    message: String
) {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("OK", onClick = onSuccessClicked)
            negativeButton("Cancel", onClick = onCancelClicked)
        }
    ) {
        title(text = title)
        message(text = message)
    }
}
