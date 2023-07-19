package com.serelik.todoapp.ui.edit.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun TodoAppComposeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val darkColorScheme = darkColorScheme(
        primary = ColorDark.labelPrimary,
        onBackground = ColorDark.backPrimary,
        secondary = ColorDark.labelSecondary,
        tertiary = ColorDark.labelTertiary,
        onPrimary = ColorDark.backPrimary,
        onSecondary = ColorDark.backSecondary,
        onSurface = ColorDark.backElevated,
        onSurfaceVariant = IndependentColor.white,
        surfaceTint = IndependentColor.white,
    )

    val lightColorScheme = lightColorScheme(
        primary = ColorLight.labelPrimary,
        background = IndependentColor.red,
        onBackground = IndependentColor.red,
        secondary = ColorLight.labelSecondary,
        tertiary = ColorLight.labelTertiary,
        onPrimary = ColorLight.backPrimary,
        onSecondary = ColorLight.backSecondary,
        surface = IndependentColor.gray,
        onSurface = ColorLight.backElevated,
        onSurfaceVariant = IndependentColor.black,
        surfaceTint = IndependentColor.black,
    )

    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Type.Typography,
        content = content
    )
}
