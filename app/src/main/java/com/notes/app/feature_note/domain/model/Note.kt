package com.notes.app.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.notes.app.ui.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val url: String,
    val dateCreated: Long?,
    val dateModified: Long,
    val color: Int,
    @PrimaryKey
    val id: Int? = null
) {
    companion object {
        val noteColors = listOf(Yellow, LightBlue, RedPink, Purple, Green)
    }
}


class InvalidNoteException(message: String) : Exception(message)
