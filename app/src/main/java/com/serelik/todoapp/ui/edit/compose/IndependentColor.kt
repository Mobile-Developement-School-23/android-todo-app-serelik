package com.serelik.todoapp.ui.edit.compose

import androidx.compose.ui.graphics.Color

object IndependentColor {
    val green = Color(0xFF34C759)
    val blue = Color(0xFF007AFF)
    val gray = Color(0xFF8E8E93)
    val white = Color(0xFFFFFFFF)
    val red = Color(0xFFFF453A)
    val black = Color(0xFF000000)

    fun getGrayLight(isDark: Boolean): Color {
        return if (isDark)
            ColorDark.grayLight
        else ColorLight.grayLight
    }

    fun getDisabled(isDark: Boolean): Color {
        return if (isDark)
            ColorDark.labelDisable
        else ColorLight.labelDisable
    }
}