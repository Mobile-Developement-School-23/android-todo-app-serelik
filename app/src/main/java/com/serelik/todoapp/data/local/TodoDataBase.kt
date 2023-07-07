package com.serelik.todoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.serelik.todoapp.data.local.entities.DbContract
import com.serelik.todoapp.data.local.entities.TodoDeletedEntity
import com.serelik.todoapp.data.local.entities.TodoEntity

@Database(entities = [TodoEntity::class, TodoDeletedEntity::class], version = 2)
@TypeConverters(UuidConverters::class)
abstract class TodoDataBase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    abstract fun todoDeletedDao(): TodoDeletedDao

    companion object {

        fun createDataBase(applicationContext: Context): TodoDataBase {
            return Room.databaseBuilder(
                applicationContext,
                TodoDataBase::class.java,
                DbContract.TODO_DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}
