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

    val screenState: MutableStateFlow<TodoEditScreenState> =
        MutableStateFlow(TodoEditScreenState())

    fun loadTodoItem(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val todoItem = repository.loadTodo(id)
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

    fun save(text: String, importance: TodoItemImportance) {
        // val todoItem = _todoItem.value ?: return
        //  val newItem = todoItem.copy(text = text, importance = importance, deadline = newDeadline)
        viewModelScope.launch(Dispatchers.IO) {
            //    repository.updateTodo(newItem)
        }
    }

    fun remove() {
        //     val todoItem = _todoItem.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            //        repository.removeTodo(todoItem.id)
        }
    }
}
