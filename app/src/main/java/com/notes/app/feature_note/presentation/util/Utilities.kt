package com.notes.app.feature_note.presentation.util

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun convertTime(time: Long): String {
    val date = Date(time)
    val format: Format = SimpleDateFormat(" HH:mm a")
    return DateFormat.getDateInstance(DateFormat.FULL).format(date) + format.format(date)
}