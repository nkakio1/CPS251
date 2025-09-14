// Main package for the app if yours is different then you will have to change it to what you have.
package com.example.assn1

// Android and Jetpack Compose imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import android.util.Log

// Main activity class - entry point of the app


class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity" //Prompted Gemini to tell me how logcat works in Kotlin
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set light status bar (white icons on dark background)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        // Set up Compose UI
        setContent {
            MaterialTheme {
                HelloClass() // Call our main composable function
                Log.i(TAG, "Calls HelloClass Function")
            }
        }
    }
}

// Main composable function that displays the UI
@Composable
fun HelloClass() {
    // Column arranges children vertically
    Column(modifier = Modifier.fillMaxSize(), // Fill entire screen
        verticalArrangement = Arrangement.Center, // Center content vertically
        horizontalAlignment = Alignment.CenterHorizontally) { // Center content horizontally
        Text("Hello Class") // Display text
        Text(text = "Welcome to CPS251") // Display text
        Text(text = "Programing in Android") // Display text
    }
}

/**
 * Preview for Android Studio's design view.
 * This allows you to see the UI in the design tab without running the app.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HelloClassPreview(){
    HelloClass() // Show the same UI as the main function
}

    