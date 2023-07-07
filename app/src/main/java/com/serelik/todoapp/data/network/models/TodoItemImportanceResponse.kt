package com.serelik.todoapp.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TodoItemImportanceResponse {
    @SerialName("basic")
    NONE,

    @SerialName("low")
    LOW,

    @SerialName("important")
    HIGH
}
