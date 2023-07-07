package com.serelik.todoapp.data.local

import android.content.Context
import com.serelik.todoapp.TodoApp

class TokenStorage {

    private val sharedPref = TodoApp.context.getSharedPreferences(
        SHARED_PREF_KEY, Context.MODE_PRIVATE
    )

    fun saveToken(token: String) {
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPref.getString(TOKEN_KEY, null)
    }

    fun hasToken(): Boolean {
        return sharedPref.contains(TOKEN_KEY)
    }

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val SHARED_PREF_KEY = "SHARED_PREF_KEY_TOKEN"
    }

}