package com.example.assn_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

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
    var isRunning by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableIntStateOf(25 * 60) }
    var sessionLength by remember { mutableIntStateOf(25) }
    var completedSessions by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Countdown timer logic
        LaunchedEffect(isRunning) {
            while (isRunning && timeRemaining > 0) {
                delay(1000)
                timeRemaining--
                if (timeRemaining <= 0) {
                    isRunning = false
                    completedSessions++
                }
            }
        }

        // Timer display
        TimerDisplay(timeRemaining = timeRemaining, sessionLength = sessionLength)

            Spacer(modifier = Modifier.height(32.dp))

        // Timer controls
        TimerControls(
            isRunning = isRunning,
            onToggleTimer = {
                if (!isRunning) {
                    timeRemaining = sessionLength * 60
                }
                isRunning = !isRunning
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Session length settings
        SessionSettings(
            sessionLength = sessionLength,
            onSessionLengthChange = { newLength ->
                isRunning = false
                sessionLength = newLength
                timeRemaining = newLength * 60
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display completed sessions
        Text(
            text = "Completed sessions: $completedSessions",
            fontSize = 20.sp,
            fontWeight = Bold
        )
    }
}
/*
@Composable
fun showScreen(isShowing: Boolean = false) {
    if (isShowing) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "TImer Complete!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = Bold
            )


        }

        LaunchedEffect(percentDone) {
            if (!isRunning && percentDone == 100) {
                showScreen = true
                delay(2000)
                showScreen = false
            }
            */



@Composable
fun TimerDisplay(
    timeRemaining: Int,
    sessionLength: Int
) {
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val progress = 1f - (timeRemaining.toFloat() / (sessionLength * 60f))
    val percentDone = (progress * 100).toInt().coerceIn(0, 100)
    var showScreen by remember { mutableStateOf(false) }
    LaunchedEffect(percentDone) {
        if (percentDone == 100) {
            showScreen = true
            delay(2000L) // 2 seconds
            showScreen = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Study Timer",
            fontSize = 24.sp,
            fontWeight = Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Text(
            text = String.format(Locale.ROOT, "%02d:%02d", minutes, seconds),
            fontSize = 48.sp,
            fontWeight = Bold,


        )
        Text(

                text = "$percentDone% Complete",
        fontSize = 18.sp,
        fontWeight = Bold,
        modifier = Modifier.alpha(0.6f),
        )
    }



    }


@Composable
fun TimerControls(
    isRunning: Boolean,
    onToggleTimer: () -> Unit
) {
    Button(
        onClick = onToggleTimer,
        modifier = Modifier
            .height(50.dp)
             .width(120.dp)
    ) {
        Text(text = if (isRunning) "Stop" else "Start")

    }
}

@Composable
fun SessionSettings(
    sessionLength: Int,
    onSessionLengthChange: (Int) -> Unit
) {
    Text("Session Length:$sessionLength Minutes",
        modifier = Modifier.padding(bottom = 16.dp),
        fontSize = 20.sp, fontWeight = Bold

    )
    val options = listOf(1, 15, 25, 45)

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { minutes ->
            Button(
                onClick = { onSessionLengthChange(minutes) },
                modifier = Modifier.width(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (minutes == sessionLength) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = "$minutes",
                    color = if (minutes == sessionLength) Color.White else Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudyTimerPreview() {
    StudyTimerApp()
}



/*

        LaunchedEffect(isRunning) {
            while (isRunning && timeRemaining > 0) {
                delay(1000L)
                timeRemaining--
                if (timeRemaining <= 0) {
                    isRunning = false
                    completedSessions++
                }
            }
        }

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val progress = 1f - (timeRemaining.toFloat() / (sessionLength * 60f))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        // Display time remaining
        Text(
            text = String.format(Locale.ROOT,"%02d:%02d", minutes, seconds),
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

    LaunchedEffect(isRunning) {
        Text(
            text = String.format($minutes, $seconds)
        )
        while (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
            if (timeRemaining <= 0) {
                isRunning = false
            }


            while (isRunning === true) {
        Text($minutes:$seconds, style = MaterialTheme.typography.displayLarge)
        thread.sleep(1)

        Thread.sleep(1000)
        timeRemaining--
        if (timeRemaining === 0) {
            isRunning = false
        }
*/
