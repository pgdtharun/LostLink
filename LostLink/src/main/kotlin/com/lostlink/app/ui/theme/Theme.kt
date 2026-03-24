package com.lostlink.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val PrimaryGreen = Color(0xFF16a34a)
private val PrimaryBlue = Color(0xFF2563eb)
private val PrimaryLightGreen = Color(0xFF4ade80)
private val PrimaryLightBlue = Color(0xFF60a5fa)

private val AccentGreen = Color(0xFF059669)
private val AccentBlue = Color(0xFF3b82f6)

private val SuccessColor = Color(0xFF10b981)
private val WarningColor = Color(0xFFf59e0b)
private val DangerColor = Color(0xFFef4444)

private val White = Color(0xFFffffff)
private val Black = Color(0xFF000000)
private val GlassWhite = Color(0xBFFFFFFF) // Semi-transparent white
private val GlassBlack = Color(0xBF111827) // Semi-transparent black
private val AcrylicGreen = Color(0xCCF0FDF4) // Semi-transparent background green
private val AcrylicDark = Color(0xCC0f172a) // Semi-transparent background dark

private val Gray50 = Color(0xFFf8fafc)
private val Gray900 = Color(0xFF0f172a)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = White,
    primaryContainer = PrimaryLightGreen,
    onPrimaryContainer = Gray900,
    secondary = PrimaryBlue,
    onSecondary = White,
    secondaryContainer = PrimaryLightBlue,
    onSecondaryContainer = Gray900,
    tertiary = WarningColor,
    onTertiary = White,
    error = DangerColor,
    onError = White,
    background = AcrylicGreen,
    onBackground = Gray900,
    surface = GlassWhite,
    onSurface = Gray900,
    outline = Color(0xFFcbd5e1).copy(alpha = 0.5f)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLightGreen,
    onPrimary = Gray900,
    primaryContainer = PrimaryGreen,
    onPrimaryContainer = White,
    secondary = PrimaryLightBlue,
    onSecondary = Gray900,
    secondaryContainer = PrimaryBlue,
    onSecondaryContainer = White,
    tertiary = WarningColor,
    onTertiary = Black,
    error = DangerColor,
    onError = White,
    background = AcrylicDark,
    onBackground = White,
    surface = GlassBlack,
    onSurface = White,
    outline = Color(0xFF475569).copy(alpha = 0.5f)
)

@Composable
fun LostLinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = LostLinkTypography,
        content = content
    )
}

val LostLinkGradient = Brush.linearGradient(
    colors = listOf(PrimaryGreen, PrimaryBlue)
)

val LostLinkLightGradient = Brush.linearGradient(
    colors = listOf(PrimaryLightGreen, PrimaryLightBlue)
)
