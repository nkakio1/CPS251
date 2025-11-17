package com.example.roomdatabasedemo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(viewModel: NoteViewModel) {
    val notes by viewModel.notes.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    var titleTouched by remember { mutableStateOf(false) }
    var contentTouched by remember { mutableStateOf(false) }

    var titleHadFocus by remember { mutableStateOf(false) }
    var contentHadFocus by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isFormValid = title.isNotBlank() && content.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Material Notes") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingNote = null
                    title = ""
                    content = ""
                    titleTouched = false
                    contentTouched = false
                    titleHadFocus = false
                    contentHadFocus = false
                },
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New note")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.large, // 16.dp corners
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = if (editingNote == null) "Create New Note" else "Edit Note",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { state ->
                                if (state.isFocused) {
                                    titleHadFocus = true
                                } else if (titleHadFocus) {
                                    titleTouched = true
                                }
                            },
                        isError = titleTouched && title.isBlank(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    if (titleTouched && title.isBlank()) {
                        Text(
                            text = "Title cannot be empty",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { state ->
                                if (state.isFocused) {
                                    contentHadFocus = true
                                } else if (contentHadFocus) {
                                    contentTouched = true
                                }
                            },
                        minLines = 3,
                        isError = contentTouched && content.isBlank(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    if (contentTouched && content.isBlank()) {
                        Text(
                            text = "Content cannot be empty",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                if (editingNote == null) {
                                    viewModel.addNote(
                                        title,
                                        content,
                                        dateFormat.format(Date())
                                    )
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Note added!")
                                    }
                                } else {
                                    viewModel.deleteNote(editingNote!!)
                                    viewModel.addNote(
                                        title,
                                        content,
                                        dateFormat.format(Date())
                                    )
                                    editingNote = null
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Note updated!")
                                    }
                                }

                                title = ""
                                content = ""
                                titleTouched = false
                                contentTouched = false
                                titleHadFocus = false
                                contentHadFocus = false
                            },
                            enabled = isFormValid,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(if (editingNote == null) "Add Note" else "Update Note")
                        }

                        if (editingNote != null) {
                            OutlinedButton(
                                onClick = {
                                    editingNote = null
                                    title = ""
                                    content = ""
                                    titleTouched = false
                                    contentTouched = false
                                    titleHadFocus = false
                                    contentHadFocus = false
                                },
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Cancel Edit")
                            }
                        }
                    }
                }
            }


            Text(
                text = "Your Notes",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(8.dp))


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp), // keep clear of FAB
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes) { note ->

                    var isFavorite by remember(note) { mutableStateOf(false) }

                    val cardColor by animateColorAsState(
                        targetValue = if (isFavorite)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface,
                        animationSpec = tween(durationMillis = 300),
                        label = "cardColor"
                    )

                    val cardElevation by animateDpAsState(
                        targetValue = if (isFavorite) 8.dp else 2.dp,
                        animationSpec = tween(durationMillis = 300),
                        label = "cardElevation"
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(animationSpec = tween(300))
                            .clickable {
                                editingNote = note
                                title = note.title
                                content = note.content
                                titleTouched = false
                                contentTouched = false
                                titleHadFocus = false
                                contentHadFocus = false
                            },
                        shape = MaterialTheme.shapes.medium, // 12.dp
                        colors = CardDefaults.cardColors(
                            containerColor = cardColor
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                note.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                note.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Last updated: ${note.date}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { isFavorite = !isFavorite }
                                    ) {
                                        Icon(
                                            imageVector = if (isFavorite)
                                                Icons.Filled.Star
                                            else
                                                Icons.Outlined.StarBorder,
                                            contentDescription = if (isFavorite)
                                                "Important"
                                            else
                                                "Not important",
                                            tint = if (isFavorite)
                                                MaterialTheme.colorScheme.secondary
                                            else
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(Modifier.width(4.dp))

                                    IconButton(onClick = { noteToDelete = note }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete note",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        AnimatedVisibility(
            visible = noteToDelete != null,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            AlertDialog(
                onDismissRequest = { noteToDelete = null },
                title = { Text("Delete Note") },
                text = {
                    Text(
                        "Are you sure you want to delete this note: \"${noteToDelete?.title}\"?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val toDelete = noteToDelete
                            if (toDelete != null) {
                                viewModel.deleteNote(toDelete)

                                if (editingNote == toDelete) {
                                    editingNote = null
                                    title = ""
                                    content = ""
                                    titleTouched = false
                                    contentTouched = false
                                    titleHadFocus = false
                                    contentHadFocus = false
                                }
                            }
                            noteToDelete = null
                        }
                    ) {
                        Text(
                            "Delete",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { noteToDelete = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
