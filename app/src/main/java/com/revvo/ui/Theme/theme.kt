package com.revvo.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RevvoDarkColorScheme = darkColorScheme(
    primary        = RevvoOrange,
    onPrimary      = RevvoWhite,
    secondary      = RevvoOrangeLight,
    onSecondary    = RevvoDark,
    background     = RevvoDark,
    onBackground   = RevvoWhite,
    surface        = RevvoSurface,
    onSurface      = RevvoWhite,
    surfaceVariant = RevvoSurfaceLight,
    outline        = RevvoGrayDark,
    error          = RevvoRed
)

@Composable
fun RevvoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RevvoDarkColorScheme,
        typography  = RevvoTypography,
        content     = content
    )
}