package com.serelik.todoapp.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            encodeDefaults = true
        }
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
        revisionInterceptor: RevisionInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addNetworkInterceptor(WrongRevisionInterceptor())
            .addInterceptor(tokenInterceptor)
            .addInterceptor(revisionInterceptor)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideContentType(): MediaType {
        return "application/json".toMediaType()

    }

    @Provides
    @Reusable
    fun provideRetrofit(httpClient: OkHttpClient, json: Json, contentType: MediaType): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(httpClient)
            .build()
    }

    @Provides
    @Reusable
    fun provideTodoApiService(retrofit: Retrofit): TodoApiService = retrofit.create()

    companion object {
        private const val baseUrl = "https://beta.mrdekk.ru/todobackend/"
    }
}