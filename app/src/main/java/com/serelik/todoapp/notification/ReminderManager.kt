package com.serelik.todoapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar
import java.util.Locale


object ReminderManager {

    fun startReminder(
        context: Context,
        reminderTime: String = "23:59",
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = createIntentAlarmManager(context, reminderId)
        val calendar = getTimeAlarmManager(reminderTime)

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
            intent
        )
    }

    fun stopReminder(
        context: Context,
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.cancel(intent)
    }

    private fun createIntentAlarmManager(context: Context, reminderId: Int): PendingIntent? {
        return Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext,
                reminderId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    private fun getTimeAlarmManager(reminderTime: String): Calendar {
        val (hours, min) = reminderTime.split(":").map { it.toInt() }
        val calendar: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 30)
        }

        if (Calendar.getInstance(Locale.ENGLISH)
                .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
        ) {
            calendar.add(Calendar.DATE, 1)
        }

        return calendar
    }

    private const val REMINDER_NOTIFICATION_REQUEST_CODE = 123

}