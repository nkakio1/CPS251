//your package name here

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * This project covers concepts from Chapter 6 lessons:
 * - "Understanding State in Compose" - for state management and updates
 * - "Stateless and Stateful Composables" - for component design patterns
 * - "Launched Effect" - for side effects and timers
 *
 * Students should review these lessons before starting:
 * - Understanding State in Compose lesson for state management
 * - Stateless and Stateful Composables lesson for component patterns
 * - Launched Effect lesson for side effects and timers
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudyTimerApp()
            }
        }
    }
}

@Composable
fun StudyTimerApp() {
    // TODO: Create state variables for the timer
    // Hint: You need variables for:
    // - isRunning (boolean for timer state)
    // - timeRemaining (int for seconds remaining)
    // - sessionLength (int for total session time in minutes)
    // - completedSessions (int for tracking completed sessions)
    // Use remember and mutableStateOf for each
    // See "Understanding State in Compose" lesson for examples of state management

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Call the TimerDisplay composable here
        // Pass the timeRemaining and sessionLength as parameters
        // See "Stateless and Stateful Composables" lesson for examples of stateless components

        Spacer(modifier = Modifier.height(32.dp))

        // TODO: Call the TimerControls composable here
        // Pass the isRunning state and a lambda to toggle the timer
        // See "Stateless and Stateful Composables" lesson for examples of stateful components

        Spacer(modifier = Modifier.height(32.dp))

        // TODO: Call the SessionSettings composable here
        // Pass the sessionLength and a lambda to update it
        // See "Understanding State in Compose" lesson for examples of state updates

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Display the completed sessions count
        // Use Text composable with the completedSessions variable
        // See "Understanding State in Compose" lesson for examples of displaying state
    }

    // TODO: Use LaunchedEffect to create the countdown timer
    // Hint: The effect should run when isRunning is true
    // Inside the effect, use delay(1000) and update timeRemaining
    // Don't forget to handle when the timer reaches zero!
    // See "Launched Effect" lesson for examples of side effects and timers
}

@Composable
fun TimerDisplay(
    timeRemaining: Int,
    sessionLength: Int
) {
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val progress = 1f - (timeRemaining.toFloat() / (sessionLength * 60))
    
    // TODO: Create a stateless timer display component
    // Display the time remaining in MM:SS format
    // Use Text composables (no CircularProgressIndicator needed)
    // See "Stateless and Stateful Composables" lesson for examples of stateless components
    /* This code will show the percent completed
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val progress = 1f - (timeRemaining.toFloat() / (sessionLength * 60))*/
}

@Composable
fun TimerControls(
    isRunning: Boolean,
    onToggleTimer: () -> Unit
) {
    // TODO: Create stateful timer control button
    // Show Play button when stopped, Reset button when running (works like a toggle)
    // Use Button composable with appropriate text
    // See "Stateless and Stateful Composables" lesson for examples of stateful components
}

@Composable
fun SessionSettings(
    sessionLength: Int,
    onSessionLengthChange: (Int) -> Unit
) {
    // TODO: Create session length configuration
    // Allow users to set custom session lengths (5, 15, 25, 45 minutes)
    // Use Button composables for each option
    // Highlight the currently selected length
    // Display the current session length value
    // Make sure the buttons show the correct minute values: 5m, 15m, 25m, 45m
    // Use appropriate button sizing (width = 70.dp) to display text properly
    // See "Understanding State in Compose" lesson for examples of state management and updates
}

@Preview(showBackground = true)
@Composable
fun StudyTimerPreview() {
    StudyTimerApp()
}