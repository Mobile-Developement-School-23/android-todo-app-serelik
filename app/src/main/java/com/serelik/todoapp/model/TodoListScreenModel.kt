package com.serelik.todoapp.model

data class TodoListScreenModel(
    val items: List<TodoUiBaseItem>,
    val isDoneVisible: Boolean,
    val doneCount: Int
)
