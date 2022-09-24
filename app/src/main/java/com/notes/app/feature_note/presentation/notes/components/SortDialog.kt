package com.notes.app.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.notes.app.feature_note.domain.util.NoteOrder
import com.notes.app.feature_note.domain.util.OrderType


@Composable
fun SortDialog(
    state: Boolean,
    noteOrder: NoteOrder,
    onDismissRequest: () -> Unit,
    onOrderChange: (noteOrder: NoteOrder) -> Unit
) {
    if (state) {
        AlertDialog(backgroundColor = MaterialTheme.colors.secondary, onDismissRequest = {
            onDismissRequest()
        }, title = null, text = null, shape = RoundedCornerShape(20.dp), buttons = {
            Column(
                modifier = Modifier.padding(40.dp, 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                CustomRadioButton(
                    text = "Title",
                    selected = noteOrder is NoteOrder.Title,
                    onSelect = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) })
                CustomRadioButton(
                    text = "Color",
                    selected = noteOrder is NoteOrder.Color,
                    onSelect = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) })
                CustomRadioButton(
                    text = "Date Created",
                    selected = noteOrder is NoteOrder.DateCreated,
                    onSelect = { onOrderChange(NoteOrder.DateCreated(noteOrder.orderType)) })
                CustomRadioButton(
                    text = "Date Modified",
                    selected = noteOrder is NoteOrder.DateModified,
                    onSelect = { onOrderChange(NoteOrder.DateModified(noteOrder.orderType)) })
                Divider(modifier = Modifier.padding(vertical = 5.dp))
                CustomRadioButton(
                    text = "Ascending",
                    selected = noteOrder.orderType is OrderType.Ascending,
                    onSelect = { onOrderChange(noteOrder.copy(OrderType.Ascending)) })
                CustomRadioButton(
                    text = "Descending",
                    selected = noteOrder.orderType is OrderType.Descending,
                    onSelect = { onOrderChange(noteOrder.copy(OrderType.Descending)) })
            }
        }, properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true))
    }
}