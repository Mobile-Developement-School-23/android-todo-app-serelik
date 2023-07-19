package com.serelik.todoapp.extensions

import androidx.annotation.StringRes
import com.serelik.todoapp.R
import com.serelik.todoapp.model.ThemeType

@StringRes
fun ThemeType.getStringRes() = when (this) {
    ThemeType.SYSTEM -> R.string.theme_system
    ThemeType.DARK -> R.string.theme_dark
    ThemeType.LIGHT -> R.string.theme_light
}
