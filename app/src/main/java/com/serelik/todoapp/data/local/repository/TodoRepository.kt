package com.serelik.todoapp.data.local.repository

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.serelik.todoapp.data.local.LocalDataSource
import com.serelik.todoapp.data.local.TodoEntityMapper
import com.serelik.todoapp.data.local.entities.TodoDeletedEntity
import com.serelik.todoapp.data.local.entities.TodoEntity
import com.serelik.todoapp.data.network.NetworkMapper
import com.serelik.todoapp.data.network.TodoApiService
import com.serelik.todoapp.data.network.models.TodoItemListBody
import com.serelik.todoapp.data.workers.SyncListTodoWorker
import com.serelik.todoapp.data.workers.WorkRepository
import com.serelik.todoapp.di.AppScope
import com.serelik.todoapp.model.NewTodo
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoListScreenModel
import com.serelik.todoapp.model.TodoUiBaseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@AppScope
class TodoRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val todoApiService: TodoApiService,
    private val databaseMapper: TodoEntityMapper,
    private val networkMapper: NetworkMapper,
    private val context: Context
) {
    suspend fun loadTodo(id: String): TodoItem {
        if (id == TodoItem.NEW_TODO_ID) {
            return TodoItem()
        }

        return localDataSource.getTodo(id)
    }

    fun loadAllTodos(): Flow<TodoListScreenModel> {
        return localDataSource.getAllTodosFlow()
            .map { createTodoListScreenModel(it, isDoneVisible = true) }
    }

    fun loadAllUnDoneTodos(): Flow<TodoListScreenModel> {
        return localDataSource.loadAllUnDoneTodos()
            .map { createTodoListScreenModel(it, isDoneVisible = false) }
    }

    suspend fun removeTodo(id: String) {
        planSync()
        localDataSource.deleteById(id)
    }

    suspend fun updateTodo(todoItem: TodoItem) {
        val entity =
            databaseMapper.fromDomainTypeToEntityType(todoItem.copy(modified = LocalDateTime.now()))

        planSync()
        localDataSource.save(entity)
    }

    suspend fun changedStateDone(item: TodoItem, isDone: Boolean) {
        planSync()
        localDataSource.changedStateDone(item.id, isDone)
    }

    private fun createTodoListScreenModel(
        list: List<TodoItem>,
        isDoneVisible: Boolean
    ): TodoListScreenModel {
        val doneCount = list.count { it.isDone }
        val listWithNewTodoItem = mutableListOf<TodoUiBaseItem>()
        listWithNewTodoItem.addAll(list)
        listWithNewTodoItem.add(NewTodo)
        return TodoListScreenModel(listWithNewTodoItem, isDoneVisible, doneCount)
    }

    suspend fun synchronizeList() = withContext(Dispatchers.IO) {
        val response = todoApiService.getListTodos()
        val revision = response.revision
        val todoFromNetworkList =
            response.todos.map { NetworkMapper().fromResponseToEntityType(it) }
        val deletedMap = localDataSource.getAllDeletedTodos().associateBy { it.id }
        val mergedListWithDeleted = todoFromNetworkList.filter { check(it, deletedMap) }
        val currentDataBase = localDataSource.getAllTodos().associateBy { it.id }.toMutableMap()

        for (networkItem in mergedListWithDeleted) {
            handleMergeElement(currentDataBase, networkItem)
        }

        val resultList = currentDataBase.values.toList().sortedBy { it.created }

        if (resultList == todoFromNetworkList) {
            return@withContext
        }

        val networkResultList =
            TodoItemListBody(resultList.map { networkMapper.fromEntityToResponseType(it) })
        val newResponse = todoApiService.sendToServer(revision, networkResultList)
        localDataSource.replaceAllTodo(newResponse.todos.map {
            networkMapper.fromResponseToEntityType(
                it
            )
        })
    }

    private fun planSync() {
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                SyncListTodoWorker.ONE_TIME_TAG,
                ExistingWorkPolicy.REPLACE,
                WorkRepository.syncTodoRequest()
            )
    }

    suspend fun loadAllTodosHaveTomorrowDeadline() = withContext(Dispatchers.IO) {
        localDataSource.getAllTodosHaveTomorrowDeadline()
    }

    private fun check(entity: TodoEntity, deleteMap: Map<String, TodoDeletedEntity>): Boolean {
        val todoDeletedEntity = deleteMap[entity.id] ?: return true
        if (entity.modified == null) {
            return false
        }
        return todoDeletedEntity.deletedAt < entity.modified
    }

    private fun handleMergeElement(
        currentDataBase: MutableMap<String, TodoEntity>,
        networkItem: TodoEntity
    ) {
        if (currentDataBase.contains(networkItem.id)) {
            val databaseItem = currentDataBase[networkItem.id] ?: return
            if ((databaseItem.modified ?: 0) <= (networkItem.modified ?: 0)) {
                currentDataBase[networkItem.id] = networkItem
            }
        } else {
            currentDataBase[networkItem.id] = networkItem
        }
    }
}
