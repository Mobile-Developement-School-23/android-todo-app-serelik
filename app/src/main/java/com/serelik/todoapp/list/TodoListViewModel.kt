package com.serelik.todoapp.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serelik.todoapp.data.local.repository.TodoRepository
import com.serelik.todoapp.model.TodoListScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {
    private val repository = TodoRepository

    private var isDoneVisible = false

    private var loadTodoJob: Job? = null

    private val _liveData: MutableLiveData<TodoListScreenModel> = MutableLiveData()
    val liveData: LiveData<TodoListScreenModel> = _liveData

    fun changedStateDone(itemId: String, isDone: Boolean) {
        viewModelScope.launch {
            repository.changedStateDone(itemId, isDone)
        }
    }

    fun remove(itemId: String) {
        viewModelScope.launch {
            repository.removeTodo(itemId)
        }
    }

    fun changeDoneVisibility() {
        loadTodoJob?.cancel()
        isDoneVisible = !isDoneVisible

        val flow = if (isDoneVisible) {
            repository.loadAllTodos()
        } else {
            repository.loadAllUnDoneTodos()
        }

        loadTodoJob = viewModelScope.launch {
            flow.collect {
                _liveData.postValue(it)
            }
        }
    }
}