package com.letsleep.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SoftLavender,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3E8FF),
    onPrimaryContainer = TextPrimary,
    secondary = SoftMint,
    onSecondary = TextPrimary,
    secondaryContainer = Color(0xFFDDF5E8),
    onSecondaryContainer = TextPrimary,
    background = CreamBackground,
    onBackground = TextPrimary,
    surface = CreamSurface,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF0E6DA),
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFFD8CFC4),
)

@Composable
fun LetSleepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // 首版固定温暖浅色，避免夜间误触深色。
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = LetSleepTypography,
        content = content,
    )
}
