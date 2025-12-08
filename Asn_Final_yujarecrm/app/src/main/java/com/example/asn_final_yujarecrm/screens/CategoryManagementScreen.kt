package com.example.asn_final_yujarecrm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.asn_final_yujarecrm.Data.Category
import com.example.asn_final_yujarecrm.Data.TransactionType
import androidx.core.graphics.toColorInt
import com.example.asn_final_yujarecrm.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(viewModel: FinanceViewModel, navController: NavController) {
    val categories by viewModel.allCategories.collectAsState()
    val transactions by viewModel.allTransactions.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }
    var showDeleteError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Categories", color = Color(0xFF1D1B20)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // This is where the error was. It is fixed now.
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Category",
                            tint = Color(0xFF1D1B20)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8DEF8))
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onDelete = {
                        val hasTransactions = transactions.any { it.categoryId == category.id }
                        if (hasTransactions) {
                            showDeleteError = true
                        } else {
                            categoryToDelete = category
                        }
                    }
                )
            }
        }

        if (showAddDialog) {
            AddCategoryDialog(
                onDismiss = { showAddDialog = false },
                onSave = { name, type, color ->
                    viewModel.addCategory(Category(name = name, type = type, color = color))
                    showAddDialog = false
                }
            )
        }

        if (categoryToDelete != null) {
            AlertDialog(
                onDismissRequest = { categoryToDelete = null },
                title = { Text("Delete Category") },
                text = { Text("Are you sure you want to delete \"${categoryToDelete?.name}\"? This action cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = {
                        categoryToDelete?.let { viewModel.deleteCategory(it) }
                        categoryToDelete = null
                    }) { Text("Delete", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { categoryToDelete = null }) { Text("Cancel") }
                }
            )
        }

        if (showDeleteError) {
            AlertDialog(
                onDismissRequest = { showDeleteError = false },
                title = { Text("Cannot Delete Category") },
                text = { Text("Cannot delete this category because it has associated transactions. Please delete or reassign all transactions in this category first.") },
                confirmButton = {
                    TextButton(onClick = { showDeleteError = false }) { Text("OK") }
                }
            )
        }
    }
}

@Composable
fun CategoryItem(category: Category, onDelete: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9FA)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(category.color.toColorInt()), CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(category.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text(category.type.name, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFB3261E))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddCategoryDialog(onDismiss: () -> Unit, onSave: (String, TransactionType, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }

    val colorPalette = listOf("#6200EE", "#03DAC5", "#3700B3", "#018786", "#000000", "#6750A4", "#B3261E")
    var selectedColor by remember { mutableStateOf(colorPalette[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Category") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Type", style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { selectedType = TransactionType.EXPENSE },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedType == TransactionType.EXPENSE) Color(0xFFE8DEF8) else Color.Transparent,
                            contentColor = if (selectedType == TransactionType.EXPENSE) Color(0xFF1D1B20) else Color.Gray
                        ),
                        border = if (selectedType != TransactionType.EXPENSE) BorderStroke(1.dp, Color.Gray) else null
                    ) {
                        Text("Expense")
                    }

                    Button(
                        onClick = { selectedType = TransactionType.INCOME },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedType == TransactionType.INCOME) Color(0xFFE8DEF8) else Color.Transparent,
                            contentColor = if (selectedType == TransactionType.INCOME) Color(0xFF1D1B20) else Color.Gray
                        ),
                        border = if (selectedType != TransactionType.INCOME) BorderStroke(1.dp, Color.Gray) else null
                    ) {
                        Text("Income")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Color", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colorPalette.forEach { colorHex ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(colorHex.toColorInt()), CircleShape)
                                .border(
                                    width = if (selectedColor == colorHex) 3.dp else 0.dp,
                                    color = Color.Black,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = colorHex }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name, selectedType, selectedColor) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}