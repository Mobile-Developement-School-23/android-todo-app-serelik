package com.serelik.todoapp.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.serelik.todoapp.Constant
import com.serelik.todoapp.data.local.repository.TodoRepository

class DeleteTodoWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(
        context,
        workerParams
    ) {
    private val repository = TodoRepository

    override suspend fun doWork(): Result {
        return try {
            val idTodo = inputData.getString(Constant.ID_KEY_WORKER) ?: return Result.success()
            repository.removeTodoOnServer(idTodo)
            Result.success()
        } catch (error: Throwable) {
            Result.retry()
        }
    }
}