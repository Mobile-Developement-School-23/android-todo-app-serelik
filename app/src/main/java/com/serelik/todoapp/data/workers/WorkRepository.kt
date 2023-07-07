package com.serelik.todoapp.data.workers

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import com.serelik.todoapp.data.local.entities.TodoEntity
import com.serelik.todoapp.data.network.NetworkMapper
import java.util.concurrent.TimeUnit

object WorkRepository {
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val mapper = NetworkMapper()

    fun loadListRequest() = OneTimeWorkRequest

    fun syncTodoRequest() = OneTimeWorkRequest
        .Builder(SyncListTodoWorker::class.java)
        .setInitialDelay(1, timeUnit = TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    private fun createDataForUpdate(todoEntity: TodoEntity): Data {
        val networkModel = mapper.fromEntity(todoEntity)
        //val json = NetworkModule.json.encodeToString(networkModel)

        return Data.Builder().build()//.putString(Constant.BODY_KEY_WORKER, json).build()
    }

    fun loadListRequestPeriodical() = PeriodicWorkRequest
        .Builder(SyncListTodoWorker::class.java, 8, TimeUnit.HOURS)
        .setConstraints(constraints)
        .setInitialDelay(8, TimeUnit.HOURS)
        .build()


}