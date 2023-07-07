package com.serelik.todoapp.model

import java.time.LocalDate
import java.time.LocalDateTime

data class TodoItem(
    val id: String = "",
    val text: String = "",
    val importance: TodoItemImportance = TodoItemImportance.NONE,
    val deadline: LocalDate? = null,
    val isDone: Boolean = false,
    val created: LocalDateTime = LocalDateTime.now(),
    val modified: LocalDateTime? = null,
)