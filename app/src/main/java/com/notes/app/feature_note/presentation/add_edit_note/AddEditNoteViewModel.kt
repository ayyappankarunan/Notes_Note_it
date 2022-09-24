package com.notes.app.feature_note.presentation.add_edit_note

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.notes.app.feature_note.domain.model.InvalidNoteException
import com.notes.app.feature_note.domain.model.Note
import com.notes.app.feature_note.domain.use_case.NoteUseCases
import com.notes.app.feature_note.presentation.util.DELETE_NOTE_CLICKED
import com.notes.app.feature_note.presentation.util.SAVE_NOTE_CLICKED
import com.notes.app.feature_note.presentation.util.convertTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Title"))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Start Typing..."))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteUrl = mutableStateOf("")
    val noteUrl: MutableState<String> = _noteUrl

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow

    private var currentNoteId: Int? = null
    private var noteCreatedDate: Long? = null

    private val _showDateModified = mutableStateOf(false)
    val showDateModified: MutableState<Boolean> = _showDateModified

    var dateModified: String? = null

    var oldNote: Note? = null

    init {
        savedStateHandle.get<Int>("noteID")?.let {
            if (it != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(it)?.also { note ->
                        oldNote = note
                        currentNoteId = note.id
                        _noteUrl.value = note.url
                        noteCreatedDate = note.dateCreated
                        _showDateModified.value = true
                        dateModified = convertTime(note.dateModified)
                        _noteTitle.value = _noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun checkIsNoteUnSaved(): Boolean {
        if (oldNote != null) {
            oldNote.also {
                if (it?.title != _noteTitle.value.text
                    || it.content != _noteContent.value.text
                    || it.color != _noteColor.value
                    || it.url != _noteUrl.value
                )
                    return true
            }
        } else {
            if (_noteTitle.value.text.isNotBlank()
                || _noteContent.value.text.isNotBlank()
                || _noteUrl.value.isNotBlank()
            ) {
                return true
            }
        }
        return false
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.EnteredUrl -> {
                _noteUrl.value = event.value
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeNoteColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        if (checkIsNoteUnSaved()) {
                            sendAnalytics(SAVE_NOTE_CLICKED)
                            noteUseCases.insertNote(
                                Note(
                                    title = noteTitle.value.text,
                                    content = noteContent.value.text,
                                    url = noteUrl.value,
                                    dateCreated = if (noteCreatedDate == null)
                                        System.currentTimeMillis()
                                    else
                                        noteCreatedDate,
                                    dateModified = System.currentTimeMillis(),
                                    color = noteColor.value,
                                    id = currentNoteId
                                )
                            )
                        }
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                e.message ?: "Try again after sometime"
                            )
                        )
                    }
                }
            }
            is AddEditNoteEvent.DeleteNote -> {
                sendAnalytics(DELETE_NOTE_CLICKED)
                viewModelScope.launch {
                    if (oldNote != null)
                        noteUseCases.deleteNote(oldNote!!)
                }
            }
        }
    }

    private fun sendAnalytics(event: String, param: Bundle? = null) {
        firebaseAnalytics.logEvent(event, param)
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }

}