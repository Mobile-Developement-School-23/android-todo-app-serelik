package com.serelik.todoapp.data.local

import android.content.Context
import com.serelik.todoapp.model.ThemeType

class ThemeStorage(context: Context) {

    private val sharedPref = context.getSharedPreferences(
        FILE_NAME,
        Context.MODE_PRIVATE
    )

    fun saveTheme(themeType: ThemeType) {
        with(sharedPref.edit()) {
            putInt(THEME_KEY, themeType.ordinal)
            apply()
        }
    }

    fun getTheme(): ThemeType {
        val ordinal = sharedPref.getInt(THEME_KEY, 0)
        return ThemeType.values()[ordinal]
    }

    companion object {
        const val FILE_NAME = "FILE_NAME"
        const val THEME_KEY = "THEME_KEY"
    }
}
