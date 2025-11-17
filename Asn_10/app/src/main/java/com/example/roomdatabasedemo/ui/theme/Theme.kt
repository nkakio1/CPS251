package com.example.roomdatabasedemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,

    secondary = LightSecondary,
    onSecondary = LightOnSecondary,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,

    error = LightError,
    onError = LightOnError
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,

    error = DarkError,
    onError = DarkOnError
)

@Composable
fun RoomDatabaseDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
    /*
     How does the app handle dark and light themes?
      What is the purpose of defining separate color schemes for each theme?

 The app animates card colors and elevations when notes are marked
  as important. Explain the purpose of using animationSpec with tween()
  in these animations?

   When the floating action button is clicked,
   the app requests focus on a text field.
   Explain why this focus management is important for user experience?

    The app defines a custom shapes system with different corner radius
    values for different sizes (extraSmall, small, medium, large, extraLarge)
    . Explain how this hierarchical shape system contributes to visual
    consistency in the Material Design system?


 The app uses AnimatedVisibility to show and hide note content when cards are clicked.
  Explain the difference between using AnimatedVisibility versus
  simply using an if statement to conditionally show content.
  What user experience benefit does the animation provide?
     */
}
