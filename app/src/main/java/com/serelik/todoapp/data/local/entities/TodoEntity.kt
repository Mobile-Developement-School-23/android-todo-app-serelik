package com.serelik.todoapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.serelik.todoapp.model.TodoItemImportance
import java.util.UUID

@Entity(
    tableName = DbContract.Todo.TABLE_NAME
)
data class TodoEntity(
    @PrimaryKey
    @ColumnInfo(name = DbContract.Todo.COLUMN_NAME_ID)
    val id: UUID = UUID.fromString(DEFAULT_UUID),

    @ColumnInfo(name = DbContract.Todo.COLUMN_NAME_TEXT)
    val text: String,

    @ColumnInfo(name = DbContract.Todo.COLUMN_NAME_IMPORTANCE)
    val importance: TodoItemImportance = TodoItemImportance.NONE,

    @ColumnInfo(name = DbContract.Todo.COLUMN_NAME_DEADLINE)
    val deadline: Long? = null,

    @ColumnInfo(name = DbContract.Todo.COLUMN_NAME_IS_DONE)
    val isDone: Boolean = false,

    @ColumnInfo(name = DbContract.Todo.COLUMN_NAME_CREATED)
    val created: Long,

    @ColumnInfo(name = DbContract.Todo.COLUMN_NAME_MODIFIED)
    val modified: Long? = null
) {
    companion object {
        const val DEFAULT_UUID = "00000000-0000-0000-0000-000000000000"
    }
}
