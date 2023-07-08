package com.serelik.todoapp.data.local

import android.content.Context
import com.serelik.todoapp.data.local.entities.TodoDeletedEntity
import com.serelik.todoapp.data.local.entities.TodoEntity
import com.serelik.todoapp.extensions.toMillis
import com.serelik.todoapp.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val mapper: TodoEntityMapper, context: Context) {

    private val dataBase = TodoDataBase.createDataBase(applicationContext = context)

    fun getAllTodosFlow(): Flow<List<TodoItem>> =
        dataBase.todoDao().loadAllTodosFlow().map { list ->
            list.map { mapper.fromEntityToTodoItem(entity = it) }
        }

    fun getAllTodos(): List<TodoEntity> = dataBase.todoDao().loadAllTodos()

    fun getAllDeletedTodos(): List<TodoDeletedEntity> = dataBase.todoDeletedDao().loadAllTodos()

    fun loadAllUnDoneTodos(): Flow<List<TodoItem>> =
        dataBase.todoDao().loadAllUnDoneTodos()
            .map { list -> list.map { mapper.fromEntityToTodoItem(entity = it) } }

    suspend fun deleteById(id: String) {
        dataBase.todoDao().deleteById(id)

        val todoDeleteEntity = TodoDeletedEntity(
            UUID.fromString(id).toString(),
            LocalDateTime.now().toMillis()
        )
        dataBase.todoDeletedDao().insert(todoDeleteEntity)
    }

    suspend fun save(todoItem: TodoEntity) = dataBase.todoDao().insert(todoItem)

    suspend fun changedStateDone(todoId: String, isDone: Boolean) =
        dataBase.todoDao().changedStateDone(todoId, isDone)

    suspend fun getTodo(todoId: String): TodoItem =
        mapper.fromEntityToTodoItem(dataBase.todoDao().loadById(todoId))

    suspend fun replaceAllTodo(todos: List<TodoEntity>) = dataBase.todoDao().replaceAll(todos)
}
