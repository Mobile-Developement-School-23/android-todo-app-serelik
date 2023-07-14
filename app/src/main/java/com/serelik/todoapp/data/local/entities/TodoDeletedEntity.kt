package com.serelik.todoapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = DbContract.DeletedTodo.TABLE_NAME
)
data class TodoDeletedEntity(
    @PrimaryKey()
    @ColumnInfo(name = DbContract.DeletedTodo.COLUMN_NAME_ID)
    val id: String,

    @ColumnInfo(name = DbContract.DeletedTodo.COLUMN_NAME_DELETED_AT)
    val deletedAt: Long
)
