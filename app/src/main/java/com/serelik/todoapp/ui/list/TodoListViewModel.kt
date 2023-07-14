package com.serelik.todoapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serelik.todoapp.data.local.repository.TodoRepository
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoListScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private var isDoneVisible = false

    private var loadTodoJob: Job? = null

    private val _todoListScreenModel: MutableLiveData<TodoListScreenModel> = MutableLiveData()
    val todoListScreenModel: LiveData<TodoListScreenModel> = _todoListScreenModel

    private val _loadingState: MutableLiveData<LoadingStatus> = MutableLiveData()
    val loadingState: LiveData<LoadingStatus> = _loadingState

    init {
        loadListFromServer()
    }

    fun changedStateDone(item: TodoItem, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changedStateDone(item, isDone)
        }
    }

    fun remove(itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
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

        loadTodoJob = viewModelScope.launch(Dispatchers.IO) {
            flow.collect {
                _todoListScreenModel.postValue(it)
            }
        }
    }

    fun loadListFromServer() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingState.postValue(LoadingStatus.Loading)
                repository.synchronizeList()
                _loadingState.postValue(LoadingStatus.Success)
            } catch (e: Throwable) {
                e.printStackTrace()
                _loadingState.postValue(LoadingStatus.Error(e))
            }
        }
    }
}
