package com.example.asn8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.contacts.ContactViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ContactViewModel by viewModels {
        ContactViewModel.Companion.provideFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ContactScreen(viewModel = viewModel)
            }
        }
    }
}
