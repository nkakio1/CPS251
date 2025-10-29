package com.example.asn8

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contacts.ContactViewModel
import com.example.contacts.SortOrder

@Composable
fun ContactScreen(viewModel: ContactViewModel) {
    val contacts by viewModel.allContacts.collectAsState(initial = emptyList())

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }
    var showNameError by remember { mutableStateOf(false) }
    var showPhoneError by remember { mutableStateOf(false) }
    var pendingDelete: Contact? by remember { mutableStateOf(null) }

    LaunchedEffect(search) { viewModel.onSearchQueryChange(search) }

    // --- Scroll + layout setup ---
    val cfg = LocalConfiguration.current
    val screenHeightDp = cfg.screenHeightDp
    val isCompact = screenHeightDp < 520               // <-- ADDED: only scroll on short screens
    val formScroll = rememberScrollState()
    val maxFormHeight = (screenHeightDp.dp * 0.55f)    // <-- ADDED: used only when compact

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ---------- TOP BOX (FORM) ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(                                   // <-- CHANGED: apply scroll/height only when compact
                        if (isCompact) {
                            Modifier
                                .heightIn(max = maxFormHeight)
                                .verticalScroll(formScroll)
                        } else {
                            Modifier
                        }
                    )
            ) {
                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showNameError
                )
                if (showNameError) {
                    Text(
                        text = "Name cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Phone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number (10 digits)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showPhoneError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (showPhoneError) {
                    Text(
                        text = "Invalid phone number. Format should be like 999.999.9999",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        showNameError = name.isBlank()
                        showPhoneError = !viewModel.isValidPhoneNumber(phone)
                        if (!showNameError && !showPhoneError) {
                            viewModel.insert(Contact(name = name.trim(), phone = phone.trim()))
                            name = ""
                            phone = ""
                            showNameError = false
                            showPhoneError = false
                        }
                    }) { Text("Add") }

                    Button(onClick = { viewModel.onSortOrderChange(SortOrder.DESC) }) {
                        Text("Sort Desc")
                    }
                    Button(onClick = { viewModel.onSortOrderChange(SortOrder.ASC) }) {
                        Text("Sort Asc")
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Search Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Contacts:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // ---------- CONTACTS LIST (separate scroll) ----------
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // takes remaining space, scrolls independently
            ) {
                items(contacts, key = { it.id }) { contact ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = contact.name,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = contact.phone,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(onClick = { pendingDelete = contact }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }

            // ---------- DELETE DIALOG ----------
            if (pendingDelete != null) {
                AlertDialog(
                    onDismissRequest = { pendingDelete = null },
                    title = { Text("Delete Contact") },
                    text = { Text("Are you sure you want to delete this contact?") },
                    confirmButton = {
                        TextButton(onClick = {
                            pendingDelete?.let { viewModel.delete(it) }
                            pendingDelete = null
                        }) { Text("Delete") }
                    },
                    dismissButton = {
                        TextButton(onClick = { pendingDelete = null }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}
