package com.serelik.todoapp.data.local

import com.serelik.todoapp.data.local.entities.TodoEntity
import com.serelik.todoapp.extensions.toLocalDate
import com.serelik.todoapp.extensions.toLocalDateTime
import com.serelik.todoapp.extensions.toMillis
import com.serelik.todoapp.model.TodoItem
import java.util.UUID
import javax.inject.Inject

class TodoEntityMapper @Inject constructor() {
    fun fromEntityToTodoItem(entity: TodoEntity): TodoItem {
        return TodoItem(
            id = entity.id,
            created = entity.created.toLocalDateTime(),
            text = entity.text,
            importance = entity.importance,
            deadline = entity.deadline?.toLocalDate(),
            isDone = entity.isDone,
            modified = entity.modified?.toLocalDateTime()

        )
    }

    fun fromDomainTypeToEntityType(todoItem: TodoItem): TodoEntity {
        return TodoEntity(
            id = if (todoItem.id == TodoItem.NEW_TODO_ID) {
                UUID.randomUUID().toString()
            } else {
                todoItem.id
            },
            created = todoItem.created.toMillis(),
            text = todoItem.text,
            importance = todoItem.importance,
            deadline = todoItem.deadline?.toMillis(),
            isDone = todoItem.isDone,
            modified = todoItem.modified?.toMillis()
        )
    }
}
