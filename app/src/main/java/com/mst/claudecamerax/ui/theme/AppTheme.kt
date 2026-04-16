package com.mst.claudecamerax.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary             = androidx.compose.ui.graphics.Color(0xFF1565C0),
    onPrimary           = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    primaryContainer    = androidx.compose.ui.graphics.Color(0xFFD6E4FF),
    onPrimaryContainer  = androidx.compose.ui.graphics.Color(0xFF001B3E),
    secondary           = androidx.compose.ui.graphics.Color(0xFF1976D2),
    onSecondary         = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    background          = androidx.compose.ui.graphics.Color(0xFFF5F7FA),
    onBackground        = androidx.compose.ui.graphics.Color(0xFF1A1C1E),
    surface             = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onSurface           = androidx.compose.ui.graphics.Color(0xFF1A1C1E),
    onSurfaceVariant    = androidx.compose.ui.graphics.Color(0xFF44474E),
)

private val DarkColors = darkColorScheme(
    primary             = androidx.compose.ui.graphics.Color(0xFF90CAF9),
    onPrimary           = androidx.compose.ui.graphics.Color(0xFF003062),
    primaryContainer    = androidx.compose.ui.graphics.Color(0xFF004488),
    onPrimaryContainer  = androidx.compose.ui.graphics.Color(0xFFD6E4FF),
    secondary           = androidx.compose.ui.graphics.Color(0xFF64B5F6),
    onSecondary         = androidx.compose.ui.graphics.Color(0xFF003258),
    background          = androidx.compose.ui.graphics.Color(0xFF1A1C1E),
    onBackground        = androidx.compose.ui.graphics.Color(0xFFE2E2E6),
    surface             = androidx.compose.ui.graphics.Color(0xFF25272B),
    onSurface           = androidx.compose.ui.graphics.Color(0xFFE2E2E6),
    onSurfaceVariant    = androidx.compose.ui.graphics.Color(0xFFC4C6CF),
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content     = content
    )
}