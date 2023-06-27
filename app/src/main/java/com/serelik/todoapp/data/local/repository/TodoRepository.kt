package com.serelik.todoapp.data.local.repository

import com.serelik.todoapp.data.local.LocalDataSource
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.model.TodoListScreenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object TodoRepository {

    private val localDataSource = LocalDataSource()

     suspend fun loadTodo(id: String): TodoItem {
        if (id == "")
            return TodoItem()

        return localDataSource.getTodo(id)
    }

    fun loadAllTodos(): Flow<TodoListScreenModel> {
        return localDataSource.getAllTodos().map { createTodoListScreenModel(it, isDoneVisible = true) }
    }

    fun loadAllUnDoneTodos(): Flow<TodoListScreenModel> {
        return localDataSource.loadAllUnDoneTodos().map { createTodoListScreenModel(it, isDoneVisible = false) }
    }

    suspend   fun removeTodo(id: String) {
        localDataSource.deleteById(id)
    }

    suspend  fun updateTodo(todoItem: TodoItem) {
        localDataSource.save(todoItem)
    }

    suspend fun changedStateDone(itemId: String, isDone: Boolean) {
        localDataSource.changedStateDone(itemId, isDone)
    }

    private fun createTodoListScreenModel(
        list: List<TodoItem>,
        isDoneVisible: Boolean
    ): TodoListScreenModel {
        val doneCount = list.count { it.isDone }
        return TodoListScreenModel(list, isDoneVisible, doneCount)
    }

}