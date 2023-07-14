package com.serelik.todoapp.model

import java.time.LocalDate
import java.time.LocalDateTime

data class TodoItem(
    val id: String = NEW_TODO_ID,
    val text: String = "",
    val importance: TodoItemImportance = TodoItemImportance.NONE,
    val deadline: LocalDate? = null,
    val isDone: Boolean = false,
    val created: LocalDateTime = LocalDateTime.now(),
    val modified: LocalDateTime? = null
) : TodoUiBaseItem {
    companion object {
        const val NEW_TODO_ID: String = "-1"
    }
}
