package com.serelik.todoapp.data.local

import androidx.room.TypeConverter
import com.serelik.todoapp.data.local.entities.TodoEntity
import java.util.UUID

class UuidConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID): String? {
        if (uuid.toString() == TodoEntity.DEFAULT_UUID)
            return UUID.randomUUID().toString()

        return uuid.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String?): UUID {
        string?.let {
            return UUID.fromString(string)
        }
        return UUID.fromString(TodoEntity.DEFAULT_UUID)
    }

}
