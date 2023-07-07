package com.serelik.todoapp.data.workers

import android.content.Context
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
            Result.retry()
        }
    }

    companion object {
        const val ONE_TIME_TAG = "one_time_tag"
        const val PERIODICAL_TAG = "upload_from_server_periodical"
    }
}
