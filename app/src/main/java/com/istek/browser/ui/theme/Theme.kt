package com.istek.browser.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Slate950 = Color(0xFF020617)
val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate400 = Color(0xFF94A3B8)
val Slate200 = Color(0xFFE2E8F0)

val IstekCyan = Color(0xFF00A3E0)
val IstekBlue = Color(0xFF005DA8)
val ShieldRed = Color(0xFFEF4444)
val ShieldOrange = Color(0xFFF97316)
val RewardYellow = Color(0xFFF59E0B)
val TrackerGreen = Color(0xFF10B981)

private val DarkColorScheme = darkColorScheme(
    primary = IstekCyan,
    onPrimary = Color.White,
    secondary = IstekBlue,
    onSecondary = Color.White,
    background = Slate950,
    onBackground = Slate200,
    surface = Slate900,
    onSurface = Slate200,
    surfaceVariant = Slate800,
    onSurfaceVariant = Slate400,
    error = ShieldRed
)

private val LightColorScheme = lightColorScheme(
    primary = IstekBlue,
    onPrimary = Color.White,
    secondary = IstekCyan,
    onSecondary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Slate700,
    error = ShieldRed
)

@Composable
fun IstekBrowserTheme(
    darkTheme: Boolean = true, // Default to sleek dark browser theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
