package com.serelik.todoapp.data.local

import android.content.Context
import javax.inject.Inject

/**         ↓ Storage for token  */
class TokenStorage @Inject constructor(context: Context) {

    private val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )

    fun saveToken(token: String) {
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun removeToken() {
        with(sharedPref.edit()) {
            remove(TOKEN_KEY)
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
