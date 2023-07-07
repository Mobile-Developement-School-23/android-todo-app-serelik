package com.serelik.todoapp.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serelik.todoapp.data.local.repository.TodoRepository
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoItemImportance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class TodoEditViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    var newDeadline: LocalDate? = null

    private val _todoItem: MutableLiveData<TodoItem> = MutableLiveData()
    val todoItem: LiveData<TodoItem> = _todoItem

    fun loadTodoItem(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val todoItem = repository.loadTodo(id)
            _todoItem.postValue(todoItem)
            newDeadline = todoItem.deadline
        }
    }

    fun save(text: String, importance: TodoItemImportance) {
        val todoItem = _todoItem.value ?: return
        val newItem = todoItem.copy(text = text, importance = importance, deadline = newDeadline)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(newItem)
        }
    }

    fun remove() {
        val todoItem = _todoItem.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTodo(todoItem.id)
        }
    }
}
