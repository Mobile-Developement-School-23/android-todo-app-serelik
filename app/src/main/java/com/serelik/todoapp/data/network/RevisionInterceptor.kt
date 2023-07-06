package com.serelik.todoapp.data.network

import com.serelik.todoapp.data.local.RevisionStorage
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.closeQuietly
import javax.inject.Inject

class RevisionInterceptor @Inject constructor(private val revisionStorage: RevisionStorage) :
    Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .header(HEADER_KEY, revisionStorage.getRevision().toString())
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val HEADER_KEY = "X-Last-Known-Revision"
    }
}
