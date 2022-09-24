package com.notes.app.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun BasicDialog(
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    confirmText: String,
    dismissText: String,
    confirmRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h6
            )
        },
        text = {
            Text(
                text = description, color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body1
            )
        },
        confirmButton = {
            Text(
                text = confirmText, color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .clickable { confirmRequest() }
                    .padding(bottom = 15.dp, end = 4.dp)
            )
        },
        dismissButton = {
            Text(
                text = dismissText, color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .clickable { onDismissRequest() }
                    .padding(bottom = 15.dp, end = 6.dp)
            )
        },
        shape = RoundedCornerShape(6.dp),
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}