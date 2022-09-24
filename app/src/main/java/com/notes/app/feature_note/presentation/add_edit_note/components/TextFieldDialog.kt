package com.notes.app.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldDialog(
    onDismissRequest: () -> Unit,
    defaultText: String = "",
    title: String,
    icon: ImageVector,
    confirmText: String,
    dismissText: String,
    confirmRequest: (String) -> Unit,
) {
    var text by remember { mutableStateOf(TextFieldValue(defaultText)) }
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = null,
        text = null,
        shape = RoundedCornerShape(6.dp),
        buttons = {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "icon",
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h6,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.primary,
                        backgroundColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text(text = "add url here") },
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.align(Alignment.End)) {
                    Text(
                        text = dismissText,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clickable { onDismissRequest() }
                    )
                    Text(
                        text = confirmText,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clickable() {
                                confirmRequest(text.text)
                            },
                    )
                }
            }
        }
    )
}