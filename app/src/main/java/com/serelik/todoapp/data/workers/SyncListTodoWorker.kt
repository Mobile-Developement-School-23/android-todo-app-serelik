package com.serelik.todoapp.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.serelik.todoapp.data.local.repository.TodoRepository

class SyncListTodoWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: TodoRepository
) : CoroutineWorker(
    context,
    workerParams
) {


    override suspend fun doWork(): Result {
        return try {
            repository.synchronizeList()

            Result.success()
        } catch (error: Throwable) {
            error.printStackTrace()
            Log.e("Worker check", error.message.toString())
            Result.retry()
        }
    }
}