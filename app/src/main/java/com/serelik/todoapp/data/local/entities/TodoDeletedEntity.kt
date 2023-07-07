package com.serelik.todoapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = DbContract.DeletedTodo.TABLE_NAME
)
data class TodoDeletedEntity(
    @PrimaryKey()
    @ColumnInfo(name = DbContract.DeletedTodo.COLUMN_NAME_ID)
    val id: UUID,

    @ColumnInfo(name = DbContract.DeletedTodo.COLUMN_NAME_DELETED_AT)
    val deletedAt: Long
)
