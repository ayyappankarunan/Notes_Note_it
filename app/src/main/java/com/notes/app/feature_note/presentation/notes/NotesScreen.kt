package com.notes.app.feature_note.presentation.notes

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.notes.app.feature_note.presentation.notes.components.NoteItem
import com.notes.app.feature_note.presentation.notes.components.SortDialog
import com.notes.app.feature_note.presentation.util.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(navController: NavController, viewModel: NotesViewModel = hiltViewModel()) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var sortDialogState by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.sendAnalytics(ADD_NOTE_CLICKED)
                navController.navigate(Screen.AddEditNoteScreen.route)
            }, backgroundColor = MaterialTheme.colors.secondary) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add note",
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    actionColor = MaterialTheme.colors.primary,
                    snackbarData = data,
                    contentColor = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.secondary
                )
            }
        }
    ) {
        Column {
            TopAppBar(title = {
                Text(
                    text = "Notes - Note it",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h5
                )
            }, actions = {
                IconButton(onClick = {
                    viewModel.sendAnalytics(SORT_NOTE_CLICKED)
                    sortDialogState = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "sort notes",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }, elevation = 0.dp, backgroundColor = MaterialTheme.colors.background)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes, key = { it.id!! }) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .animateItemPlacement()
                            .clickable {
                                viewModel.sendAnalytics(NOTE_CLICKED)
                                navController.navigate(
                                    Screen.AddEditNoteScreen.route +
                                            "?noteID=${note.id}&noteColor=${note.color}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        }
                    )
                }
            }
        }
        SortDialog(
            state = sortDialogState,
            noteOrder = state.noteOrder,
            onDismissRequest = { sortDialogState = false },
            onOrderChange = { viewModel.onEvent(NotesEvent.Order(it)) })
    }
}
