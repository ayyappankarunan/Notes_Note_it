package com.notes.app.feature_note.presentation.add_edit_note

import com.notes.app.R
import com.notes.app.feature_note.presentation.util.UiText

data class NoteTextFieldState(
    var text: String = "",
    var hint: UiText.StringResource = UiText.StringResource(R.string.empty_string),
    var isHintVisible: Boolean = true
)
