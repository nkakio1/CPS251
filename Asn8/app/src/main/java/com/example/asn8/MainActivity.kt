package com.example.asn8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme

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











    /*
    1: Having the list update as you type the
    letters in the search box was not discussed in class. Go through the code and explain how you implemented that functionality?

    TWhen you type in the search box, every letter you type is sent to the ViewModel.
The ViewModel  asks DAO) for contacts whose names contain those letters.
The UI listens for those updates and automatically redraws the contact list as the results change.

Result: every keystroke updates the searchQuery StateFlow, which causes a new DAO Flow
findContacts("%sh%")) to be collected and the UI list recomposes.
    2:


     */
}
