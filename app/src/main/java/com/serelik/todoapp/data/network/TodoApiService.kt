package com.serelik.todoapp.data.network

import com.serelik.todoapp.data.network.models.TodoItemListResponse
import com.serelik.todoapp.data.network.models.TodoItemNetworkResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApiService {
    @GET("list")
    suspend fun getListTodos(): TodoItemListResponse

    @PATCH("list")
    suspend fun sendToServer(
        @Body body: TodoItemListResponse
    ): TodoItemListResponse

    @POST("list")
    suspend fun addTodo(
        @Body body: TodoItemNetworkResponse
    ): TodoItemNetworkResponse

    @PUT("list/{todo_id}")
    suspend fun updateTodo(
        @Path(value = "todo_id") id: String,
        @Body body: TodoItemNetworkResponse
    ): TodoItemNetworkResponse

    @DELETE("list/{todo_id}")
    suspend fun deleteById(
        @Path(value = "todo_id") id: String
    ): TodoItemNetworkResponse


}