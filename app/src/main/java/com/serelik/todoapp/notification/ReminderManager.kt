package com.serelik.todoapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.serelik.todoapp.data.local.DeadlineNotificationStorage
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


class ReminderManager @Inject constructor(
    private val context: Context,
    private val deadlineNotificationStorage: DeadlineNotificationStorage
) {

    fun startReminder(
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val reminderTime = deadlineNotificationStorage.getDeadlineTimeNotification()
        if (reminderTime == DO_NOT_NOTIFY)
            return
        val intent = createIntentAlarmManager(reminderId)
        val calendar = getTimeAlarmManager(reminderTime)

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
            intent
        )
    }

    fun stopReminder(
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(createIntentAlarmManager(reminderId))
    }

    private fun createIntentAlarmManager(reminderId: Int): PendingIntent? {
        return Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext,
                reminderId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    private fun getTimeAlarmManager(reminderTime: Int): Calendar {
        val hours = reminderTime / 60
        val minutes = reminderTime % 60
        Log.d("tag", "$hours, $minutes")
        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND, 1)
        }

        if (Calendar.getInstance(Locale.ENGLISH)
                .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
        ) {
            calendar.add(Calendar.DATE, 1)
        }

        return calendar
    }

    companion object {
        private const val REMINDER_NOTIFICATION_REQUEST_CODE = 123
        const val DO_NOT_NOTIFY = -1
    }

}