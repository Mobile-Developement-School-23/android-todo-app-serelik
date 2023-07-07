package com.serelik.todoapp.data.workers

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

object WorkRepository {
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val constraintsPeriodical = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(true)
        .build()

    fun syncTodoRequest() = OneTimeWorkRequest
        .Builder(SyncListTodoWorker::class.java)
        .setInitialDelay(20, timeUnit = TimeUnit.SECONDS)
        .setConstraints(constraints)
        .build()

    fun loadListRequestPeriodical() = PeriodicWorkRequest
        .Builder(SyncListTodoWorker::class.java, 8, TimeUnit.HOURS)
        .setConstraints(constraintsPeriodical)
        .setInitialDelay(8, TimeUnit.HOURS)
        .build()
}