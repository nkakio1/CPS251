package com.example.asn_b4final_todropthe50gradeirecievedforbeingdumblol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.asn_b4final_todropthe50gradeirecievedforbeingdumblol.ui.theme.Asn_b4final_todropthe50gradeirecievedforbeingdumblolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Asn_b4final_todropthe50gradeirecievedforbeingdumblolTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    SlidingNumberUI()
                }
            }
        }
    }
}
