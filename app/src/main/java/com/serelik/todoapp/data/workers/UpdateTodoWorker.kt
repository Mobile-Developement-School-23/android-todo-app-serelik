package com.serelik.todoapp.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.serelik.todoapp.Constant
import com.serelik.todoapp.data.local.repository.TodoRepository
import com.serelik.todoapp.data.network.NetworkModule
import com.serelik.todoapp.data.network.models.TodoItemResponse
import kotlinx.serialization.decodeFromString

class UpdateTodoWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(
        context,
        workerParams
    ) {
    private val repository = TodoRepository

    override suspend fun doWork(): Result {
        return try {

            val todoString =
                inputData.getString(Constant.BODY_KEY_WORKER) ?: return Result.success()

            val todo = NetworkModule.json.decodeFromString<TodoItemResponse>(todoString)

            repository.updateTodoOnServer(todo)
            Result.success()
        } catch (error: Throwable) {
            Result.retry()
        }
    }
}