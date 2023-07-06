package com.serelik.todoapp.data.local

import android.content.Context
import com.serelik.todoapp.TodoApp
import com.serelik.todoapp.data.local.entities.TodoEntity
import com.serelik.todoapp.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val mapper: TodoEntityMapper, context: Context) {

    private val dataBase = TodoDataBase.createDataBase(applicationContext = context)

    fun getAllTodos(): Flow<List<TodoItem>> = dataBase.todoDao().loadAllTodos().map { list ->
        list.map { mapper.fromEntity(entity = it) }
    }

    fun loadAllUnDoneTodos(): Flow<List<TodoItem>> =
        dataBase.todoDao().loadAllUnDoneTodos()
            .map { list -> list.map { mapper.fromEntity(entity = it) } }

    suspend fun deleteById(id: String) {
        dataBase.todoDao().deleteById(id)
    }

    suspend fun save(todoItem: TodoEntity) = dataBase.todoDao().insert(todoItem)

    suspend fun changedStateDone(todoId: String, isDone: Boolean) =
        dataBase.todoDao().changedStateDone(todoId, isDone)

    suspend fun getTodo(todoId: String): TodoItem =
        mapper.fromEntity(dataBase.todoDao().loadById(todoId))

    suspend fun deleteAllTodo() = dataBase.todoDao().deleteAll()

    suspend fun saveAll(newTodoList: List<TodoEntity>) = dataBase.todoDao().insertAll(newTodoList)


}