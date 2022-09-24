package com.notes.app.feature_note.presentation.add_edit_note

data class NoteTextFieldState(
    var text: String = "",
    var hint: String = "",
    var isHintVisible: Boolean = true
)
