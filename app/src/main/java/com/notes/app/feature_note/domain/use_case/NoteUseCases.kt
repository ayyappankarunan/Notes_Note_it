package com.notes.app.feature_note.domain.use_case

data class NoteUseCases(
    val getNotes: GetNotes,
    val insertNote: InsertNote,
    val getNote: GetNote,
    val deleteNote: DeleteNote,
)