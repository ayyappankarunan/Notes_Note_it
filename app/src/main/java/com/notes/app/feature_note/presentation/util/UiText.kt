package com.notes.app.feature_note.presentation.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    class StringResource(@StringRes val resId: Int, vararg val args: Any) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(resId, *args)
        }
    }

}
