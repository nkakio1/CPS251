package com.example.asn_b4final_todropthe50gradeirecievedforbeingdumblol.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF0D47A1),

    secondary = Color(0xFF03A9F4),
    onSecondary = Color.White,

    background = Color(0xFFFDFBFF),
    onBackground = Color(0xFF1B1B1F),

    surface = Color(0xFFFDFBFF),
    onSurface = Color(0xFF1B1B1F),

    surfaceVariant = Color(0xFFE0E3EB),
    onSurfaceVariant = Color(0xFF43474E),


    tertiary = Color(0xFF00897B),
    onTertiary = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF00315F),
    primaryContainer = Color(0xFF0D47A1),
    onPrimaryContainer = Color(0xFFE3F2FD),

    secondary = Color(0xFF4FC3F7),
    onSecondary = Color(0xFF003546),

    background = Color(0xFF121318),
    onBackground = Color(0xFFE2E2E6),

    surface = Color(0xFF121318),
    onSurface = Color(0xFFE2E2E6),

    surfaceVariant = Color(0xFF414651),
    onSurfaceVariant = Color(0xFFC5C6D0),

    tertiary = Color(0xFF26A69A),
    onTertiary = Color(0xFF00201B)
)

@Composable
fun Asn_b4final_todropthe50gradeirecievedforbeingdumblolTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
