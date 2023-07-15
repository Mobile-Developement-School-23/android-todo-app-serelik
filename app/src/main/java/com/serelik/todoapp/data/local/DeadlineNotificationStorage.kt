package com.serelik.todoapp.data.local

import android.content.Context
import javax.inject.Inject

class DeadlineNotificationStorage @Inject constructor(context: Context) {

    private val sharedPref = context.getSharedPreferences(
        FILE_NAME,
        Context.MODE_PRIVATE
    )

    fun saveDeadlineTimeNotification(time: Int) {
        with(sharedPref.edit()) {
            putInt(Deadline_NOTIFICATION_KEY, time)
            apply()
        }
    }

    fun getDeadlineTimeNotification(): Int {
        return sharedPref.getInt(Deadline_NOTIFICATION_KEY, -1)
    }

    companion object {
        const val FILE_NAME = "FILE_NAME"
        const val Deadline_NOTIFICATION_KEY = "Deadline_NOTIFICATION_KEY"
    }
}
