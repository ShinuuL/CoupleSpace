package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CosmicColorScheme = darkColorScheme(
    primary = CyanGlow,
    onPrimary = Color(0xFF00363A),
    primaryContainer = NeonCyan,
    onPrimaryContainer = Color(0xFF004F54),
    secondary = StarryLavender,
    onSecondary = Color(0xFF1C0062),
    secondaryContainer = DeepPurple,
    onSecondaryContainer = StarryLavender,
    background = SpaceNavy,
    onBackground = OnSurfaceText,
    surface = SpaceNavy,
    onSurface = OnSurfaceText,
    surfaceVariant = CosmicContainer,
    onSurfaceVariant = OnSurfaceVariantText,
    outline = OnSurfaceVariantText,
    outlineVariant = Color(0xFF3B494B),
    error = CosmicError,
    onError = Color(0xFF690005)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme for the cosmic aesthetic
    dynamicColor: Boolean = false, // Disable dynamic colors to keep our premium starry theme
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = CosmicColorScheme,
        typography = Typography,
        content = content
    )
}
