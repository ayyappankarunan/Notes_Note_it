package com.notes.app.feature_note.presentation.notes.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.notes.app.feature_note.domain.model.Note
import com.notes.app.ui.theme.LightRed


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    val state = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToStart) {
            onDeleteClick()
        }
        true
    })
    SwipeToDismiss(
        state = state,
        modifier = modifier,
        directions = setOf(
            DismissDirection.EndToStart
        ),
        dismissThresholds = {
            FractionalThreshold(0.1f)
        },
        background = {
            val color by animateColorAsState(
                when (state.targetValue) {
                    DismissValue.Default -> Color.Transparent
                    else -> LightRed
                }
            )
            val alignment = Alignment.CenterEnd
            val icon = Icons.Default.Delete

            val scale by animateFloatAsState(
                if (state.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
                    .padding(10.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Delete Icon",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(8.dp)
            ) {
                ListItem(note = note)
            }
        }
    )
}

@Composable
fun ListItem(note: Note) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(8.dp)
                        .padding(start = 4.dp)
                        .background(color = Color(note.color), shape = RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = note.title,
                    color = MaterialTheme.colors.primary,
                    overflow = TextOverflow.Ellipsis, softWrap = true,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2
                )
            }
            Text(
                text = note.content, color = MaterialTheme.colors.primary,
                maxLines = 5, overflow = TextOverflow.Ellipsis, softWrap = true,
                style = MaterialTheme.typography.body1, modifier = Modifier.padding(5.dp)
            )
        }
    }
}