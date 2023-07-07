package com.serelik.todoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.serelik.todoapp.data.local.entities.TodoDeletedEntity

@Dao
interface TodoDeletedDao {
    @Query("SELECT * FROM todo_deleted")
    fun loadAllTodos(): List<TodoDeletedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoDeletedEntity)
}