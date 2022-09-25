package com.notes.app.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import com.notes.app.feature_note.presentation.util.UiText

@Composable
fun TransparentTextFields(
    text: String,
    hint: UiText.StringResource,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions(),
    maxLines: Int = Int.MAX_VALUE
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                },
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            keyboardActions = keyboardActions,
            maxLines = maxLines,
        )
        if (isHintVisible) {
            Text(text = hint.asString(), style = textStyle, color = MaterialTheme.colors.primary)
        }
    }
}