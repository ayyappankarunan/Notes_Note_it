package com.notes.app.feature_note.presentation.notes

import android.os.Bundle
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.notes.app.feature_note.domain.model.Note
import com.notes.app.feature_note.domain.use_case.NoteUseCases
import com.notes.app.feature_note.domain.util.NoteOrder
import com.notes.app.feature_note.domain.util.OrderType
import com.notes.app.feature_note.presentation.util.DELETE_NOTE_CLICKED
import com.notes.app.feature_note.presentation.util.RESTORE_NOTE_CLICKED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    var getNotedJob: Job? = null

    private var recentlyDeletedNote: Note? = null

    init {
        getNotes(NoteOrder.DateModified(OrderType.Descending))
    }

    fun onEvent(notesEvent: NotesEvent) {
        when (notesEvent) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == notesEvent.noteOrder::class &&
                    state.value.noteOrder.orderType == notesEvent.noteOrder.orderType
                ) {
                    return
                }
                getNotes(notesEvent.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                sendAnalytics(DELETE_NOTE_CLICKED)
                viewModelScope.launch {
                    noteUseCases.deleteNote(notesEvent.note)
                    recentlyDeletedNote = notesEvent.note
                }
            }
            is NotesEvent.RestoreNote -> {
                sendAnalytics(RESTORE_NOTE_CLICKED)
                viewModelScope.launch {
                    noteUseCases.insertNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
        }
    }

    fun sendAnalytics(event: String, param: Bundle? = null) {
        firebaseAnalytics.logEvent(event, param)
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotedJob?.cancel()
        getNotedJob = noteUseCases.getNotes(noteOrder).onEach { notes ->
            _state.value = state.value.copy(
                notes = notes,
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }
}