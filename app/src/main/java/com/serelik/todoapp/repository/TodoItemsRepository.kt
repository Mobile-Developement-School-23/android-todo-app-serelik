package com.serelik.todoapp.repository

import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import java.time.LocalDate

class TodoItemsRepository {

    fun getItems() = listOf(
        TodoItem(
            id = "1",
            text = "First todo",
            created = LocalDate.now()
        ),
        TodoItem(
            id = "2",
            text = "Second todo",
            deadline = LocalDate.of(2001, 8, 22),
            importance = TodoItemImportance.MEDIUM,
            created = LocalDate.of(2001, 8, 20)
        ),
        TodoItem(
            id = "3",
            text = "Very looooooooooooooooooooooooooooooooooooooooooooooooong todo",
            importance = TodoItemImportance.HIGH,
            isDone = true,
            created = LocalDate.of(2050, 6, 10),
            deadline = LocalDate.of(2051, 8, 22),
        ),
        TodoItem(
            id = "4",
            text = "Fourth todo",
            created = LocalDate.of(2071, 8, 20)
        )
    )
}