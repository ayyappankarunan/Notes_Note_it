package com.notes.app.feature_note.domain.util

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType) : NoteOrder(orderType)
    class Color(orderType: OrderType) : NoteOrder(orderType)
    class DateCreated(orderType: OrderType) : NoteOrder(orderType)
    class DateModified(orderType: OrderType) : NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Color -> Color(orderType)
            is DateCreated -> DateCreated(orderType)
            is DateModified -> DateModified(orderType)
        }
    }
}