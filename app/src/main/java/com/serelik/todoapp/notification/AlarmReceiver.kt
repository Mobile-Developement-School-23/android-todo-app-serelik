package com.serelik.todoapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.serelik.todoapp.R
import com.serelik.todoapp.component
import com.serelik.todoapp.data.local.repository.TodoRepository
import com.serelik.todoapp.extensions.getStringImportance
import com.serelik.todoapp.model.TodoItem
import com.serelik.todoapp.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: TodoRepository

    @Inject
    lateinit var reminderManager: ReminderManager

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onReceive(context: Context, intent: Intent?) {
        context.component.inject(this)

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager


        coroutineScope.launch {
            val tomorrowTodos = repository.loadAllTodosHaveTomorrowDeadline()
            tomorrowTodos.forEachIndexed { index, todoItem ->
                showNotification(
                    context,
                    todoItem,
                    notificationManager,
                    index
                )
            }
        }

        reminderManager.startReminder()
    }

    private suspend fun showNotification(

        context: Context,
        todoItem: TodoItem,
        notificationManager: NotificationManager,
        index: Int
    ) = withContext(Dispatchers.Main) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, REMINDERS_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.getString(todoItem.importance.getStringImportance()))
            .setContentText(todoItem.text)
            .setSmallIcon(R.drawable.ic_app)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(todoItem.text)
            )

        notificationManager.notify(index, builder.build())
    }

    companion object {
        const val REMINDERS_NOTIFICATION_CHANNEL_ID = "REMINDERS_NOTIFICATION_CHANNEL_ID"
    }
}
