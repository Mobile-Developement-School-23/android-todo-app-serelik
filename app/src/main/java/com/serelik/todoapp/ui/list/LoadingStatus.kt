package com.serelik.todoapp.ui.list

sealed interface LoadingStatus {
    object Loading : LoadingStatus
    object Success : LoadingStatus
    class Error(val throwable: Throwable) : LoadingStatus
}
