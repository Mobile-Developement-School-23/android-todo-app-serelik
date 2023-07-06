package com.serelik.todoapp.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.serelik.todoapp.TodoApp
import com.serelik.todoapp.data.local.repository.TodoRepository
import com.serelik.todoapp.data.workers.WorkRepository
import com.serelik.todoapp.edit.TodoEditViewModel
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoListScreenModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

    private var isDoneVisible = false

    private var loadTodoJob: Job? = null

    private val _liveData: MutableLiveData<TodoListScreenModel> = MutableLiveData()
    val liveData: LiveData<TodoListScreenModel> = _liveData

    val flowLoading = repository.loadingFlow

    init {
        loadListFromServer()
    }

    fun changedStateDone(item: TodoItem, isDone: Boolean) {
        viewModelScope.launch {
            repository.changedStateDone(item, isDone)
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


    fun loadListFromServer() {
        viewModelScope.launch {
            try {
                repository.synchronizeList()
            } catch (_: Throwable) {

            }
        }
    }

    class Factory @Inject constructor(
        private val repository: TodoRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
            TodoListViewModel::class.java -> TodoListViewModel(repository)
            else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
        } as T

    }

}