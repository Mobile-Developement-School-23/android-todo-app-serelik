package com.serelik.todoapp.data.network

import com.serelik.todoapp.data.network.models.TodoItemListBody
import com.serelik.todoapp.data.network.models.TodoItemListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface TodoApiService {
    @GET("list")
    suspend fun getListTodos(): TodoItemListResponse

    @PATCH("list")
    suspend fun sendToServer(
        @Header("X-Last-Known-Revision") revision: Long,
        @Body body: TodoItemListBody
    ): TodoItemListResponse
}
