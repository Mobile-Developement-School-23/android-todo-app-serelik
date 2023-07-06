package com.serelik.todoapp.data.network

import com.serelik.todoapp.TodoApp
import com.serelik.todoapp.data.local.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val tokenStorage: TokenStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenStorage.getToken()
        val request = original.newBuilder()
            .header(AUTHORIZATION_KEY, "OAuth $token")
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }
}
