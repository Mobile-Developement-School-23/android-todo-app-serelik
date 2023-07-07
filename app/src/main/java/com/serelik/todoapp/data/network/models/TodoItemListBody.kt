package com.serelik.todoapp.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemListBody(
    @SerialName("list")
    val todos: List<TodoItemResponse>,
)