package com.serelik.todoapp.list

import androidx.lifecycle.ViewModel
import com.serelik.todoapp.repository.TodoItemsRepository

class TodoListViewModel : ViewModel() {
    val repository = TodoItemsRepository
    val toDoItemFlow = repository.todoItemsFlow

    fun changedStateDone(itemId: String, isDone: Boolean) {
        repository.changedStateDone(itemId, isDone)
    }

    fun remove(itemId: String) {
        repository.removeTodo(itemId)
    }

    fun changeDoneVisibility() {
        repository.changeDoneVisibility()
    }


}