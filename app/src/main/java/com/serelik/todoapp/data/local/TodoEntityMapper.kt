package com.serelik.todoapp.data.local

import com.serelik.todoapp.data.local.entities.TodoEntity
import com.serelik.todoapp.extensions.toLocalDate
import com.serelik.todoapp.extensions.toLocalDateTime
import com.serelik.todoapp.extensions.toMillis
import com.serelik.todoapp.model.TodoItem
import java.util.UUID

class TodoEntityMapper {
    fun fromEntity(entity: TodoEntity): TodoItem {

        return TodoItem(
            id = entity.id.toString(),
            created = entity.created.toLocalDateTime(),
            text = entity.text,
            importance = entity.importance,
            deadline = entity.deadline?.toLocalDate(),
            isDone = entity.isDone,
            modified = entity.modified?.toLocalDateTime()

        )
    }

    fun fromDomain(todoItem: TodoItem): TodoEntity {
        return TodoEntity(
            id = if (todoItem.id == "") UUID.randomUUID() else UUID.fromString(todoItem.id),
            created = todoItem.created.toMillis(),
            text = todoItem.text,
            importance = todoItem.importance,
            deadline = todoItem.deadline?.toMillis(),
            isDone = todoItem.isDone,
            modified = todoItem.modified?.toMillis()
        )
    }
}