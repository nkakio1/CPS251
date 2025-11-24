package com.example.asn_b4final_todropthe50gradeirecievedforbeingdumblol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.asn_b4final_todropthe50gradeirecievedforbeingdumblol.ui.theme.Asn_b4final_todropthe50gradeirecievedforbeingdumblolTheme

class MainActivity : ComponentActivity() {
    /*
     How does the drag gesture system determine when a tile should move?

 Why represent the empty space as 0 instead of null or a separate
  empty state? How does this choice affect the rest of the code?

 Why use LazyVerticalGrid instead of a regular Column with Row
  composables? What are the performance and code organization benefits?

 When a tile moves, which parts of the UI recompose?
  How does Compose know what to update when the puzzle state changes?

TWhat prevents invalid moves (e.g., moving a tile that isn't
 adjacent to the empty space)? Where should this validation logic live?

     */
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
