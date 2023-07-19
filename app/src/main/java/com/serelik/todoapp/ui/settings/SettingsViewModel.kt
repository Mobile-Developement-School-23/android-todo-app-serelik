package com.serelik.todoapp.ui.settings

import androidx.lifecycle.ViewModel
import com.serelik.todoapp.data.local.DeadlineNotificationStorage
import com.serelik.todoapp.notification.ReminderManager
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val deadlineNotificationStorage: DeadlineNotificationStorage,
    private val reminderManager: ReminderManager
) :
    ViewModel() {

    fun getNewDeadlineTimeNotification(): Int {
        return deadlineNotificationStorage.getDeadlineTimeNotification()
    }

    fun save(deadline: Int) {
        deadlineNotificationStorage.saveDeadlineTimeNotification(deadline)
        reminderManager.stopReminder()
        reminderManager.startReminder()
    }
}
