package com.serelik.todoapp.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serelik.todoapp.data.local.repository.TodoRepository
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class TodoEditViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private var todoItemId = TodoItem.NEW_TODO_ID

    val screenState: MutableStateFlow<TodoEditScreenState> =
        MutableStateFlow(TodoEditScreenState())

    private var todoItem = TodoItem()

    fun loadTodoItem(id: String) {
        todoItemId = id
        viewModelScope.launch(Dispatchers.IO) {
            todoItem = repository.loadTodo(id)
            screenState.value = TodoEditScreenState(
                isNew = id == TodoItem.NEW_TODO_ID,
                deadlineDate = todoItem.deadline,
                importance = todoItem.importance
            )
        }
    }

    fun setNewDeadline(deadline: LocalDate?) {
        screenState.value = screenState.value.copy(deadlineDate = deadline)
    }

    fun setNewImportance(importance: TodoItemImportance) {
        screenState.value = screenState.value.copy(importance = importance)
    }

    fun updateTodoItem(input: String) {
        screenState.value = screenState.value.copy(text = input)
    }

    fun save() {
        val state = screenState.value
        val newItem = todoItem.copy(
            text = state.text,
            importance = state.importance,
            deadline = state.deadlineDate
        )
        viewModelScope.launch(Dispatchers.IO) {
                repository.updateTodo(newItem)
        }
    }

    fun remove() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTodo(todoItemId)
        }
    }
}
