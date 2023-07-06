package com.serelik.todoapp.data.workers

import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import com.serelik.todoapp.Constant
import com.serelik.todoapp.data.local.entities.TodoEntity
import com.serelik.todoapp.data.network.NetworkMapper
import com.serelik.todoapp.data.network.NetworkModule
import kotlinx.serialization.encodeToString
import java.util.concurrent.TimeUnit

object WorkRepository {
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val mapper = NetworkMapper()

    fun loadListRequest() = OneTimeWorkRequest
        .Builder(LoadListTodoWorker::class.java)
        .setConstraints(constraints)
        .build()

    fun addTodoRequest(todoEntity: TodoEntity) = OneTimeWorkRequest
        .Builder(AddTodoWorker::class.java)
        .setInputData(createDataForUpdate(todoEntity))
        .setConstraints(constraints)
        .build()

    fun updateTodoRequest(todoEntity: TodoEntity) = OneTimeWorkRequest
        .Builder(UpdateTodoWorker::class.java)
        .setInputData(createDataForUpdate(todoEntity))
        .setConstraints(constraints)
        .build()

    fun deleteByIdRequest(id: String): OneTimeWorkRequest {

        val data = Data.Builder().putString(Constant.ID_KEY_WORKER, id).build()

        return OneTimeWorkRequest
            .Builder(DeleteTodoWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .build()
    }

    private fun createDataForUpdate(todoEntity: TodoEntity): Data {
        val networkModel = mapper.fromEntity(todoEntity)
        //val json = NetworkModule.json.encodeToString(networkModel)

        return Data.Builder().build()//.putString(Constant.BODY_KEY_WORKER, json).build()
    }

    fun loadListRequestPeriodical() = PeriodicWorkRequest
        .Builder(LoadListTodoWorker::class.java, 8, TimeUnit.HOURS)
        .setConstraints(constraints)
        .setInitialDelay(8, TimeUnit.HOURS)
        .build()


}