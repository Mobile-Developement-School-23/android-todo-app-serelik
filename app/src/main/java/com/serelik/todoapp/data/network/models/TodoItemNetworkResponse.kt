package com.serelik.todoapp.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemNetworkResponse(
    @SerialName("status")
    val status: String,
    @SerialName("element")
    val todoItemElement: TodoItemResponse,
    @SerialName("revision")
    val revision: Long
)