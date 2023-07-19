package com.serelik.todoapp.ui.edit

import com.serelik.todoapp.model.TodoItemImportance
import java.time.LocalDate

data class TodoEditScreenState(
    val isNew: Boolean = true,
    val text: String = "",
    val deadlineDate: LocalDate? = null,
    val importance: TodoItemImportance = TodoItemImportance.HIGH
)
