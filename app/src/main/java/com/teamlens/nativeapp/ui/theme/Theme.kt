package com.teamlens.nativeapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Brand = Color(0xFFE2553D)
val BrandLight = Color(0xFFFFF1ED)
val BrandDark = Color(0xFFB93A28)
val Ink = Color(0xFF1A1817)
val Muted = Color(0xFF726A64)
val MutedLight = Color(0xFFA69E97)
val Background = Color(0xFFF8F5F2)
val Surface = Color(0xFFFFFFFF)
val Surface2 = Color(0xFFFAF8F6)
val Border = Color(0xFFE8E1DA)
val Divider = Color(0xFFF0EBE6)
val Success = Color(0xFF10B981)
val SuccessTint = Color(0xFFECFDF5)
val Warning = Color(0xFFF59E0B)
val WarningTint = Color(0xFFFFFBEB)
val Danger = Color(0xFFEF4444)
val DangerTint = Color(0xFFFEF2F2)
val Info = Color(0xFF3B82F6)
val InfoTint = Color(0xFFEFF6FF)

private val colors: ColorScheme = lightColorScheme(
    primary = Brand,
    onPrimary = Color.White,
    background = Background,
    onBackground = Ink,
    surface = Surface,
    onSurface = Ink,
    outline = Border,
    secondary = Success,
    onSecondary = Color.White
)

@Composable
fun TeamLensTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
