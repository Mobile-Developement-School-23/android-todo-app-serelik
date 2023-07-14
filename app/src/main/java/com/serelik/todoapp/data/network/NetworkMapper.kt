package com.serelik.todoapp.data.network

import com.serelik.todoapp.data.local.entities.TodoEntity
import com.serelik.todoapp.data.network.models.TodoItemImportanceResponse
import com.serelik.todoapp.data.network.models.TodoItemResponse
import com.serelik.todoapp.model.TodoItemImportance
import javax.inject.Inject

class NetworkMapper @Inject constructor() {
    fun fromEntityToResponseType(entity: TodoEntity): TodoItemResponse {
        return TodoItemResponse(
            id = entity.id,
            created = entity.created,
            text = entity.text,
            importance = when (entity.importance) {
                TodoItemImportance.NONE -> TodoItemImportanceResponse.NONE
                TodoItemImportance.LOW -> TodoItemImportanceResponse.LOW
                TodoItemImportance.HIGH -> TodoItemImportanceResponse.HIGH
            },
            deadline = entity.deadline,
            isDone = entity.isDone,
            modified = entity.modified

        )
    }

    fun fromResponseToEntityType(todoItem: TodoItemResponse): TodoEntity {
        return TodoEntity(
            id = todoItem.id,
            created = todoItem.created,
            text = todoItem.text,
            importance = when (todoItem.importance) {
                TodoItemImportanceResponse.NONE -> TodoItemImportance.NONE
                TodoItemImportanceResponse.LOW -> TodoItemImportance.LOW
                TodoItemImportanceResponse.HIGH -> TodoItemImportance.HIGH
            },
            deadline = todoItem.deadline,
            isDone = todoItem.isDone,
            modified = todoItem.modified
        )
    }
}
