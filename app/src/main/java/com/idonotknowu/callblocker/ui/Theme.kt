package com.idonotknowu.callblocker.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// One accent (brand blue) + one cool-grey family. Shape rule: actions are pills, cards 20dp.
private val Light = lightColorScheme(
    primary = Color(0xFF1F6FD1),
    onPrimary = Color.White,
    background = Color(0xFFF3F7FB),
    onBackground = Color(0xFF0B1B33),
    surface = Color.White,
    onSurface = Color(0xFF0B1B33),
    onSurfaceVariant = Color(0xFF5B6B82),
    surfaceVariant = Color(0xFFE4ECF5),
    outline = Color(0xFFC9D6E5),
)

private val Dark = darkColorScheme(
    primary = Color(0xFF6AA5F0),
    onPrimary = Color(0xFF06214A),
    background = Color(0xFF0A1220),
    onBackground = Color(0xFFE8EEF7),
    surface = Color(0xFF131E31),
    onSurface = Color(0xFFE8EEF7),
    onSurfaceVariant = Color(0xFF93A3BA),
    surfaceVariant = Color(0xFF1C2A42),
    outline = Color(0xFF2B3B57),
)

val CardShape = RoundedCornerShape(20.dp)
val PillShape = RoundedCornerShape(percent = 50)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) Dark else Light,
        shapes = Shapes(medium = CardShape, large = CardShape),
        content = content,
    )
}
