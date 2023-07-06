package com.serelik.todoapp.data.local.repository

import com.serelik.todoapp.data.local.LocalDataSource
import com.serelik.todoapp.data.local.RevisionStorage
import com.serelik.todoapp.data.local.TodoEntityMapper
import com.serelik.todoapp.data.network.NetworkMapper
import com.serelik.todoapp.data.network.TodoApiService
import com.serelik.todoapp.data.network.models.TodoItemNetworkResponse
import com.serelik.todoapp.data.network.models.TodoItemResponse
import com.serelik.todoapp.di.ActivityScope
import com.serelik.todoapp.list.LoadingStatus
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoListScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@ActivityScope
class TodoRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val revisionStorage: RevisionStorage,
    private val todoApiService: TodoApiService,
    private val databaseMapper: TodoEntityMapper
) {


    private val _loadingFlow = MutableStateFlow<LoadingStatus>(LoadingStatus.Loading)
    val loadingFlow: StateFlow<LoadingStatus> = _loadingFlow

    suspend fun loadTodo(id: String): TodoItem {
        if (id == "")
            return TodoItem()

        return localDataSource.getTodo(id)
    }

    fun loadAllTodos(): Flow<TodoListScreenModel> {
        return localDataSource.getAllTodos()
            .map { createTodoListScreenModel(it, isDoneVisible = true) }
    }

    fun loadAllUnDoneTodos(): Flow<TodoListScreenModel> {
        return localDataSource.loadAllUnDoneTodos()
            .map { createTodoListScreenModel(it, isDoneVisible = false) }
    }

    suspend fun removeTodo(id: String) {
        /* WorkManager.getInstance(TodoApp.context)
             .enqueueUniqueWork(
                 "Delete_from_server_id=$id",
                 ExistingWorkPolicy.KEEP,
                 WorkRepository.deleteByIdRequest(id)
             )*/

        localDataSource.deleteById(id)
    }

    suspend fun updateTodo(todoItem: TodoItem) {
        val entity = databaseMapper.fromDomain(todoItem.copy(modified = LocalDateTime.now()))

        /*  val isUpdating = todoItem.id != ""
          val workManagerRequest: OneTimeWorkRequest
          val tag: String

          if (isUpdating) {
              tag = "update_on_server"
              workManagerRequest = WorkRepository.updateTodoRequest(entity)
          } else {
              workManagerRequest = WorkRepository.addTodoRequest(entity)
              tag = "add_on_server"
          }

          WorkManager.getInstance(TodoApp.context)
              .enqueueUniqueWork(
                  "${tag}_id=${todoItem.id}",
                  ExistingWorkPolicy.KEEP,
                  workManagerRequest
              )*/

        localDataSource.save(entity)
    }

    suspend fun changedStateDone(item: TodoItem, isDone: Boolean) {

        val entity = databaseMapper.fromDomain(item.copy(isDone = isDone))
        /*   WorkManager.getInstance(TodoApp.context)
               .enqueueUniqueWork(
                   "update_on_server_id=${item.id}",
                   ExistingWorkPolicy.KEEP,
                   WorkRepository.updateTodoRequest(entity)
               )*/

        localDataSource.changedStateDone(item.id, isDone)
    }

    suspend fun synchronizeList() = withContext(Dispatchers.IO) {
        try {
            _loadingFlow.value = LoadingStatus.Loading
            val response = todoApiService.getListTodos()
            val revision = response.revision
            revisionStorage.saveRevision(revision)
            val newTodoList =
                response.todos.map { NetworkMapper().fromNetwork(it) }
            localDataSource.deleteAllTodo()
            localDataSource.saveAll(newTodoList)
            _loadingFlow.value = LoadingStatus.Success
        } catch (e: Throwable) {
            _loadingFlow.value = LoadingStatus.Error(e)
            throw e
        }

    }

    suspend fun removeTodoOnServer(id: String) {
        val response = todoApiService.deleteById(id)
        val revision = response.revision
        revisionStorage.saveRevision(revision)
    }

    suspend fun addTodoOnServer(todoItemResponse: TodoItemResponse) {
        val body = TodoItemNetworkResponse("", todoItemResponse, 0)
        val response = todoApiService.addTodo(body)
        val revision = response.revision
        revisionStorage.saveRevision(revision)
    }

    suspend fun updateTodoOnServer(todoItemResponse: TodoItemResponse) {
        val body = TodoItemNetworkResponse("", todoItemResponse, 0)
        val response = todoApiService.updateTodo(todoItemResponse.id, body)
        val revision = response.revision
        revisionStorage.saveRevision(revision)
    }

    private fun createTodoListScreenModel(
        list: List<TodoItem>,
        isDoneVisible: Boolean
    ): TodoListScreenModel {
        val doneCount = list.count { it.isDone }
        return TodoListScreenModel(list, isDoneVisible, doneCount)
    }

}