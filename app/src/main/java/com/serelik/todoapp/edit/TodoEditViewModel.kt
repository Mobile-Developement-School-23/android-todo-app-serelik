package com.serelik.todoapp.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serelik.todoapp.data.local.repository.TodoRepository
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class TodoEditViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    var newDeadline: LocalDate? = null

    private val _todoItemLiveData: MutableLiveData<TodoItem> = MutableLiveData()
    val todoItemLiveData: LiveData<TodoItem> = _todoItemLiveData

    fun loadTodoItem(id: String) {
        viewModelScope.launch {
            val todoItem = repository.loadTodo(id)
            _todoItemLiveData.postValue(todoItem)
            newDeadline = todoItem.deadline
        }
    }

    fun save(text: String, importance: TodoItemImportance) {
        val todoItem = _todoItemLiveData.value ?: return
        val newItem = todoItem.copy(text = text, importance = importance, deadline = newDeadline)
        viewModelScope.launch {
            repository.updateTodo(newItem)
        }
    }

    fun remove() {
        val todoItem = _todoItemLiveData.value ?: return
        viewModelScope.launch {
            repository.removeTodo(todoItem.id)
        }
    }
}
