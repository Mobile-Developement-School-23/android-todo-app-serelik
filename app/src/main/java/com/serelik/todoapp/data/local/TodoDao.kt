package com.serelik.todoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.serelik.todoapp.data.local.entities.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY created DESC")
    fun loadAllTodosFlow(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo")
    suspend fun loadAllTodos(): List<TodoEntity>

    @Query("SELECT * FROM todo WHERE todo.deadline = :tomorrow ")
    suspend fun loadAllTodosHaveTomorrowDeadline(tomorrow: Long): List<TodoEntity>

    @Query("SELECT * FROM todo WHERE is_done = 0 ORDER BY created DESC ")
    fun loadAllUnDoneTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo WHERE _id = :todoId")
    suspend fun loadById(todoId: String): TodoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todos: List<TodoEntity>)

    @Query("DELETE FROM todo WHERE _id = :todoId")
    suspend fun deleteById(todoId: String)

    @Query("DELETE FROM todo")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(todos: List<TodoEntity>) {
        deleteAll()
        insertAll(todos)
    }

    @Query("UPDATE todo SET is_done = :isDone, modified = :modified  WHERE _id = :todoId")
    suspend fun changedStateDone(todoId: String, isDone: Boolean, modified: Long)
}
