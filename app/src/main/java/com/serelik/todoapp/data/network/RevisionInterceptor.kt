package com.serelik.todoapp.data.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RevisionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val url =
            originalHttpUrl.newBuilder().addQueryParameter(AUTHORIZATION_KEY,"X-Last-Known-Revision 0")
                .build()

        val request = Request.Builder().url(url).build()
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }
}
