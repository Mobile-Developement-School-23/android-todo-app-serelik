package com.serelik.todoapp.model

data class TodoListScreenModel(
    val items: List<TodoItem>,
    val isDoneVisible: Boolean,
    val doneCount: Int
)
