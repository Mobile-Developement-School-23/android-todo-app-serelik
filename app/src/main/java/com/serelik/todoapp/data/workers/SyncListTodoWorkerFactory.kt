package com.serelik.todoapp.data.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.serelik.todoapp.data.local.repository.TodoRepository
import javax.inject.Inject

class SyncListTodoWorkerFactory @Inject constructor(private val repository: TodoRepository) :
    WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncListTodoWorker::class.java.name -> SyncListTodoWorker(
                context = appContext,
                workerParams = workerParameters,
                repository = repository
            )
            else -> null
        }
    }
}
