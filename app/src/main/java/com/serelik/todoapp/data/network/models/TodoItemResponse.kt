package com.serelik.todoapp.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemResponse(
    @SerialName("id")
    val id: String = "",
    @SerialName("text")
    val text: String = "",
    @SerialName("importance")
    val importance: TodoItemImportanceResponse = TodoItemImportanceResponse.NONE,
    @SerialName("deadline")
    val deadline: Long? = null,
    @SerialName("done")
    val isDone: Boolean = false,
    @SerialName("created_at")
    val created: Long,
    @SerialName("changed_at")
    val modified: Long? = null,
)