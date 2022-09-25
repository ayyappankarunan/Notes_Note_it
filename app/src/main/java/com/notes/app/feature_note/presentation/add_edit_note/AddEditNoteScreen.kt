package com.notes.app.feature_note.presentation.add_edit_note

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.notes.app.R
import com.notes.app.feature_note.domain.model.Note
import com.notes.app.feature_note.presentation.add_edit_note.components.BasicDialog
import com.notes.app.feature_note.presentation.add_edit_note.components.TextFieldDialog
import com.notes.app.feature_note.presentation.add_edit_note.components.TransparentTextFields
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
) {

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val urlState = viewModel.noteUrl.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val noteColorAnimated = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }
    var exitDialogState by remember {
        mutableStateOf(false)
    }
    var deleteDialogState by remember {
        mutableStateOf(false)
    }
    var addEditUrlDialogState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp, 0.dp, 6.dp, 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(100.dp)
                        .height(4.dp)
                        .background(MaterialTheme.colors.secondaryVariant, RoundedCornerShape(2.dp))
                        .align(Alignment.CenterHorizontally)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Note.noteColors.forEach {
                        val color = it.toArgb()
                        val shape = RoundedCornerShape(5.dp)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(4.dp, shape)
                                .clip(shape)
                                .background(it)
                                .border(
                                    width = 2.dp, color = if (color == viewModel.noteColor.value)
                                        MaterialTheme.colors.primary
                                    else
                                        Color.Transparent, shape = shape
                                )
                                .clickable {
                                    scope.launch {
                                        noteColorAnimated.animateTo(
                                            targetValue = Color(color),
                                            animationSpec = tween(
                                                durationMillis = 500
                                            )
                                        )
                                    }
                                    viewModel.onEvent(AddEditNoteEvent.ChangeNoteColor(color))
                                }
                        ) {
                            if (color == viewModel.noteColor.value)
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = stringResource(id = R.string.selected_color),
                                    tint = MaterialTheme.colors.primary, modifier = Modifier.align(
                                        Alignment.Center
                                    )
                                )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            addEditUrlDialogState = true
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Link,
                        stringResource(id = R.string.add_url),
                        modifier = Modifier
                            .padding(14.dp)
                            .size(20.dp),
                        tint = MaterialTheme.colors.primary,
                    )
                    Text(
                        if (urlState.isEmpty()) stringResource(id = R.string.add_url) else stringResource(
                            id = R.string.edit_url
                        ),
                        style = TextStyle(
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            deleteDialogState = true
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Delete,
                        stringResource(id = R.string.delete_note),
                        modifier = Modifier
                            .padding(14.dp)
                            .size(20.dp),
                        tint = Color.Red,
                    )
                    Text(
                        stringResource(id = R.string.delete_note),
                        style = TextStyle(
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
        },
        sheetBackgroundColor = MaterialTheme.colors.secondary,
        sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
    ) {
        BackHandler(true) {
            if (sheetState.isVisible) {
                scope.launch { sheetState.hide() }
            } else if (viewModel.checkIsNoteUnSaved()) {
                exitDialogState = true
            } else navController.navigateUp()
        }
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                    backgroundColor = MaterialTheme.colors.secondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(id = R.string.save_note),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }, scaffoldState = scaffoldState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                TopAppBar(title = {}, actions = {
                    IconButton(onClick = {
                        scope.launch {
                            keyboardController?.hide()
                            sheetState.show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(id = R.string.menu),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }, navigationIcon = {
                    IconButton(onClick = {
                        if (viewModel.checkIsNoteUnSaved().not()) navController.navigateUp()
                        else exitDialogState = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }, backgroundColor = MaterialTheme.colors.background, elevation = 0.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(8.dp)
                            .padding(start = 4.dp)
                            .background(noteColorAnimated.value, shape = RoundedCornerShape(4.dp))
                    )
                    Column {
                        if (viewModel.showDateModified.value) {
                            Text(
                                text = viewModel.dateModified!!,
                                style = TextStyle(
                                    color = MaterialTheme.colors.secondaryVariant,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, 4.dp)
                            )
                        }
                        TransparentTextFields(
                            text = titleState.text,
                            hint = titleState.hint,
                            onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                            onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) },
                            singleLine = false,
                            textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                            isHintVisible = titleState.isHintVisible,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        )
                    }
                }
                if (urlState.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = stringResource(id = R.string.link),
                            tint = noteColorAnimated.value
                        )
                        Text(
                            text = urlState,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clickable {
                                    var webpage = Uri.parse(urlState)
                                    if (!urlState.startsWith("http://") && !urlState.startsWith("https://")) {
                                        Uri
                                            .parse("http://$urlState")
                                            .also { webpage = it }
                                    }
                                    val urlIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        webpage
                                    )
                                    context.startActivity(urlIntent)
                                },
                            style = TextStyle(
                                color = noteColorAnimated.value,
                                textDecoration = TextDecoration.Underline,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TransparentTextFields(
                    text = contentState.text,
                    hint = contentState.hint,
                    onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
                    onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it)) },
                    singleLine = false,
                    textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    isHintVisible = contentState.isHintVisible,
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                )
            }
        }
    }
    if (exitDialogState) {
        BasicDialog(
            onDismissRequest = { exitDialogState = false },
            title = stringResource(id = R.string.leave_note),
            description = stringResource(id = R.string.leave_note_desc),
            confirmText = stringResource(id = R.string.leave),
            dismissText = stringResource(id = R.string.cancel),
            confirmRequest = {
                exitDialogState = false
                navController.navigateUp()
            }
        )
    }
    if (addEditUrlDialogState) {
        TextFieldDialog(
            onDismissRequest = { addEditUrlDialogState = false },
            defaultText = urlState,
            title = if (urlState.isEmpty()) stringResource(id = R.string.add_url) else stringResource(
                id = R.string.edit_url
            ),
            icon = Icons.Default.Link,
            confirmText = stringResource(id = R.string.add),
            dismissText = stringResource(id = R.string.cancel),
            confirmRequest = {
                viewModel.onEvent(AddEditNoteEvent.EnteredUrl(it))
                addEditUrlDialogState = false
            }
        )
    }
    if (deleteDialogState) {
        BasicDialog(
            onDismissRequest = { deleteDialogState = false },
            title = stringResource(id = R.string.delete_note_qn),
            description = stringResource(id = R.string.delete_note_desc),
            confirmText = stringResource(id = R.string.delete),
            dismissText = stringResource(id = R.string.no),
            confirmRequest = {
                viewModel.onEvent(AddEditNoteEvent.DeleteNote)
                deleteDialogState = false
                navController.navigateUp()
            }
        )
    }
}