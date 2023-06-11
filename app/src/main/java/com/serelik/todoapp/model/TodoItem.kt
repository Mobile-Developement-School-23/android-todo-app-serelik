package com.serelik.todoapp.model

import java.time.LocalDate

data class TodoItem(
    val id: String,
    val text: String,
    val importance: TodoItemImportance = TodoItemImportance.NONE,
    val deadline: LocalDate? = null,
    val isDone: Boolean = false,
    val created: LocalDate,
    val modified: LocalDate? = null,
)