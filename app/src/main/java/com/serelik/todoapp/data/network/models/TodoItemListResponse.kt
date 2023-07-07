package com.serelik.todoapp.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemListResponse(
    @SerialName("status")
    val status: String,
    @SerialName("list")
    val todos: List<TodoItemResponse>,
    @SerialName("revision")
    val revision: Long
)
