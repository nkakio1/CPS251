package com.example.assn_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

        TimerDisplay(timeRemaining = timeRemaining, sessionLength = sessionLength)

            Spacer(modifier = Modifier.height(32.dp))

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

        SessionSettings(
            sessionLength = sessionLength,
            onSessionLengthChange = { newLength ->
                isRunning = false
                sessionLength = newLength
                timeRemaining = newLength * 60
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

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
{
            if (!isRunning && percentDone == 100) {
                showScreen = true
                delay(2000)
                showScreen = false
            }
            */



@Composable
fun TimerDisplay (timeRemaining: Int, sessionLength: Int) {
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val progress = 1f - (timeRemaining.toFloat() / (sessionLength * 60f))
    val percentDone = (progress * 100).toInt().coerceIn(0, 100)
 //   var showScreen by remember { mutableStateOf(false) }
    /*LaunchedEffect(percentDone) {
        if (percentDone == 100) {
            showScreen = true
            delay(2000L) // 2 seconds
            showScreen = false
        }
    }
*/
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
qa notes:
remember and multstateo are to manage the states of variables and such.
for example if you want things to change based on the value or data type in a variable you
need to use these to recompose things as said variable is changed. and remember hels with screen rotation
so the program doesnt reset to default values on a screen rotation, we also want this to be applied to
is running and time remaining as these should not be reset to base values
 when the screen flips or something else is recomposed

Stateless vs stateful composables are just things that hold no state, or for example
timer display is just a constantly updating variable, which we are feeding updating variables into, and
displaying the variable through. this is done to keep things separate, so the logic behind the display is
it simply displays. it also makes it accept these variables as parameters

to run the timer logic in a launched effect you have to keep that logic inside of the launched effect
its just a loop which you change the boolean state of running to determine if it should stop or go.
I actually had to do this as I tried twice without it and gave up to try is. turns out it was an issue
I was having with where i put it but thats aside the point.

seperating timer controls and session settings is also just compartmentalizing the actions and logic
of these buttons, since one controls the starting and stopping, the other will  change the session length
seperating small things like this lets me look back at it easier when I come back from a break or a day
of not working on it. and if theres an error you can clearly see which part is causing it

launched effect is basically a setting to run something during a composables uptime
for example when it stops composing it will auto cancel I believe
I simply ran a boolean into it so it only is called when isRunning is true, I beleive thats how it worked





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
