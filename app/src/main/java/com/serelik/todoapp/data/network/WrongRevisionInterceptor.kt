package com.serelik.todoapp.data.network

import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.serelik.todoapp.TodoApp
import com.serelik.todoapp.data.local.RevisionStorage
import com.serelik.todoapp.data.workers.WorkRepository
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.closeQuietly
import java.io.IOException

class WrongRevisionInterceptor : Interceptor {
/*
    private val revisionStorage = RevisionStorage()*/

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val response = chain.proceed(chain.request())
//todo fix me
/*            if (response.code == BAD_REQUEST_CODE) {
                if (response.body?.string() == BAD_REQUEST_BODY) {
                    response.body?.closeQuietly()
                    WorkManager.getInstance(TodoApp.context)
                        .enqueueUniqueWork(
                            "update_revision",
                            ExistingWorkPolicy.KEEP,
                            WorkRepository.loadListRequest()
                        )
                }
            }*/


            return response
        } catch (e: IOException) {
            Log.d("check_code_res", e.toString())

            throw e
        }
    }

    companion object {
        private const val HEADER_KEY = "X-Last-Known-Revision"
        private const val BAD_REQUEST_CODE = 400
        private const val BAD_REQUEST_BODY = "unsynchronized data"
    }
}
