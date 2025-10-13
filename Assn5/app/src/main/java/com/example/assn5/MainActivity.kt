package com.example.assn5
// your package name here

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow

/**
 * This project covers concepts from Chapter 8 lessons:
 * - "Lazy Column" - for creating scrollable contact lists
 * - "Handling Clicks and Selection" - for interactive contact selection
 * - "Combining LazyColumn and LazyRow" - for understanding list composition
 *
 * Students should review these lessons before starting:
 * - LazyColumn lesson for list implementation
 * - Clicks and Selection lesson for interactive behavior
 * - Combined lesson for understanding how lists work together
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactListApp()
                }
            }
        }
    }
}

@Composable
fun ContactListApp() {
    // Create sample contact data (25 contacts)
    val contacts = listOf(
        Contact("Jane Smith", "jane@example.com", "555-0102"),
        Contact("Bob Johnson", "bob@example.com", "555-0103"),
        Contact("Alice Brown", "alice@example.com", "555-0104"),
        Contact("Charlie Wilson", "charlie@example.com", "555-0105"),
        Contact("Diana Davis", "diana@example.com", "555-0106"),
        Contact("Eve Miller", "eve@example.com", "555-0107"),
        Contact("Frank Garcia", "frank@example.com", "555-0108"),
        Contact("Grace Lee", "grace@example.com", "555-0109"),
        Contact("Hank Martinez", "hank@example.com", "555-0110"),
        Contact("Ivy Clark", "ivy@example.com", "555-0111"),
        Contact("Jake Turner", "jake@example.com", "555-0112"),
        Contact("Karen Young", "karen@example.com", "555-0113"),
        Contact("Liam King", "liam@example.com", "555-0114"),
        Contact("Mia Wright", "mia@example.com", "555-0115"),
        Contact("Noah Scott", "noah@example.com", "555-0116"),
        Contact("Olivia Green", "olivia@example.com", "555-0117"),
        Contact("Paul Adams", "paul@example.com", "555-0118"),
        Contact("Quinn Baker", "quinn@example.com", "555-0119"),
        Contact("Rose Carter", "rose@example.com", "555-0120"),
        Contact("Sam Diaz", "sam@example.com", "555-0121"),
        Contact("Tina Evans", "tina@example.com", "555-0122"),
        Contact("Uma Foster", "uma@example.com", "555-0123"),
        Contact("Vince Grant", "vince@example.com", "555-0124"),
        Contact("Wendy Hayes", "wendy@example.com", "555-0125"),
        Contact("Xavier Ives", "xavier@example.com", "555-0126")
    )

    ContactList(contacts = contacts)
}

@Composable
fun ContactList(contacts: List<Contact>) {
    var selectedContactName by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp)
    ) {
        Text(
            text = "Contact List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val selectionText = if (selectedContactName != null) {
            "Selected: $selectedContactName"
        } else {
            "No contact selected"
        }
        Text(
            text = selectionText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(contacts) { contact ->
                val isSelected = contact.name == selectedContactName
                ContactItem(
                    contact = contact,
                    isSelected = isSelected,
                    onClick = {
                        selectedContactName = if (isSelected) null else contact.name
                    }
                )
            }
        }

        if (selectedContactName != null) {
            Button(
                onClick = { selectedContactName = null },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Clear Selection")
            }
        }
    }
}

@Composable
fun ContactItem(
    contact: Contact,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(//.padding
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val avatarBg = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            }
            val avatarTextColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }

            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(avatarBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initialsFromName(contact.name),
                    fontWeight = FontWeight.Bold,
                    color = avatarTextColor,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = contact.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = contact.phone,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

private fun initialsFromName(name: String): String {
    val parts = name.trim().split(Regex("\\s+"))
    return when {
        parts.isEmpty() -> ""
        parts.size == 1 -> parts[0].firstOrNull()?.toString()?.uppercase() ?: ""
        else -> (parts[0].firstOrNull()?.toString() ?: "") + (parts[1].firstOrNull()?.toString() ?: "")
    }.uppercase()
}

data class Contact(
    val name: String,
    val email: String,
    val phone: String
)

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactListAppPreview() {
    ContactListApp()
    /*
   Why should you use LazyColumn instead of Column for the contact list,
   and what runtime benefits does it provide for long lists?

-saves memory and improves performance, only shows what you need

When should you use rememberSaveable instead of remember for tracking a selected contact,
 and what kinds of configuration changes or process deaths does it protect against?

-to remember the state of the selected contact across configuration changes and process deaths
-will change with the state of the selected contact
-rotation, UI mode (dark/light), locale, font scale, window size changes that recreate the Activity.
-when the app is killed in background and later restored.


Explain how Modifier order (e.g., padding before vs after background) changes layout and appearance;
give one practical reason this matters for the contact item card.
- for this project specifically, having the padding go first in modifiers
-lets me to cover the entire card including the outer padding, area
-on select. they run inside out so running background().padding(16.dp) will also color the padding


How would you implement multi‑selection (data structure, toggle behavior,
and UI affordances such as a selection count and bulk actions)?
- I would impliment these by storing each contact in lists by an identifier
-to toggle them on click, I would run into lots of issues that I am unaware of, but
- It seems like you'd want to make a list for unselected contacts, and selected contacts to be
-able to toggle them easily, by moving them back and forth

var selectedNames by rememberSaveable { mutableStateOf(setOf<String>()) }
selectedNames = if (contact.name in selectedNames) selectedNames - contact.name else selectedNames + contact.name

    */
}
