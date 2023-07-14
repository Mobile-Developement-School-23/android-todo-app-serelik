package com.serelik.todoapp.data.local.entities

import android.provider.BaseColumns

object DbContract {

    const val TODO_DATABASE_NAME = "TodoApp.db"

    object Todo {
        const val TABLE_NAME = "todo"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_TEXT = "text"
        const val COLUMN_NAME_IMPORTANCE = "importance"
        const val COLUMN_NAME_DEADLINE = "deadline"
        const val COLUMN_NAME_IS_DONE = "is_done"
        const val COLUMN_NAME_CREATED = "created"
        const val COLUMN_NAME_MODIFIED = "modified"
    }

    object DeletedTodo {
        const val TABLE_NAME = "todo_deleted"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_DELETED_AT = "deleted_at"
    }
}
