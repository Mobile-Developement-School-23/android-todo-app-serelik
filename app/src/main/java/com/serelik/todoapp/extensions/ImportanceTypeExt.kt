package com.serelik.todoapp.extensions

import androidx.annotation.StringRes
import com.serelik.todoapp.R
import com.serelik.todoapp.model.TodoItemImportance

@StringRes
fun TodoItemImportance.getStringImportance() = when (this) {
    TodoItemImportance.NONE -> R.string.deadline_soon
    TodoItemImportance.LOW -> R.string.deadline_soon_low
    TodoItemImportance.HIGH -> R.string.deadline_soon_high
}
