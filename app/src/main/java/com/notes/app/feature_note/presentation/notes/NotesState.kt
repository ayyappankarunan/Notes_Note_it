package com.notes.app.feature_note.presentation.notes

import com.notes.app.feature_note.domain.model.Note
import com.notes.app.feature_note.domain.util.NoteOrder
import com.notes.app.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.DateModified(OrderType.Ascending)
)
