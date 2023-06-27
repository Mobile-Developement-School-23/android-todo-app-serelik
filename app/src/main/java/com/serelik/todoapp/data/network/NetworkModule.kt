package com.serelik.todoapp.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

object NetworkModule {
    private const val baseUrl = "https://beta.mrdekk.ru/todobackend/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val httpClient = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(TokenInterceptor())
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    private val contentType = "application/json".toMediaType()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(httpClient)
        .build()

    val todoApiService: TodoApiService = retrofit.create()
}