package com.serelik.todoapp.edit

import androidx.lifecycle.ViewModel
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import com.serelik.todoapp.repository.TodoItemsRepository
import java.time.LocalDate

class TodoEditViewModel : ViewModel() {

    //todo think about no late init
    var newDeadline: LocalDate? = null

    lateinit var todoItem: TodoItem

    private val repository = TodoItemsRepository

    fun loadTodoItem(id: String) {
        todoItem = repository.loadTodo(id) ?: TodoItem(
            id = "-1",
            created = LocalDate.now(),
            text = ""
        )

        newDeadline = todoItem.deadline
    }

    fun save(text: String, importance: TodoItemImportance) {
        val newItem = todoItem.copy(text = text, importance = importance, deadline = newDeadline)
        repository.updateTodo(newItem)
    }

    fun remove() {
        repository.removeTodo(todoItem.id)
    }
}