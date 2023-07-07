package com.serelik.todoapp.list

sealed interface LoadingStatus {
    object Loading : LoadingStatus
    object Success : LoadingStatus
    class Error(val throwable: Throwable) : LoadingStatus
}