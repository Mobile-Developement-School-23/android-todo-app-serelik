package com.serelik.todoapp.data.network

import com.serelik.todoapp.data.local.TokenStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor : Interceptor {

    private val tokenStorage = TokenStorage()

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val token = tokenStorage.getToken()
        val url = if (token != null) {
            originalHttpUrl.newBuilder().addQueryParameter(AUTHORIZATION_KEY,"OAuth $token")
                .build()
        } else return chain.proceed(original)

        val request = Request.Builder().url(url).build()
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }
}
