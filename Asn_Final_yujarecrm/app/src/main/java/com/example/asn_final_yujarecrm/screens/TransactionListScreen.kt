package com.example.finalproject.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.asn_final_yujarecrm.ViewModel.FinanceViewModel
import com.example.asn_final_yujarecrm.screens.TransactionItem
import com.example.finalproject.data.Transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(viewModel: FinanceViewModel, navController: NavController) {
    val allTransactions by viewModel.allTransactions.collectAsState()
    val categories by viewModel.allCategories.collectAsState()

    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    val displayedTransactions = if (selectedCategoryId == null) {
        allTransactions
    } else {
        allTransactions.filter { it.categoryId == selectedCategoryId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions", color = Color(0xFF1D1B20)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { showFilterDialog = true }) {
                        Text("Filter", color = Color(0xFF6750A4))
                    }
                    IconButton(onClick = { navController.navigate("add_transaction") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color(0xFF1D1B20))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8DEF8))
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            LazyColumn {
                items(displayedTransactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        showDelete = true,
                        onDelete = { transactionToDelete = transaction },
                        onClick = { navController.navigate("edit_transaction/${transaction.id}") }
                    )
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }

        // Filter Dialog
        if (showFilterDialog) {
            AlertDialog(
                onDismissRequest = { showFilterDialog = false },
                title = { Text("Filter by Category") },
                text = {
                    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCategoryId = null; showFilterDialog = false }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("All Categories", color = if (selectedCategoryId == null) Color(0xFF6750A4) else Color.Black)
                            }
                        }
                        items(categories) { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCategoryId = category.id; showFilterDialog = false }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(category.name, color = if (selectedCategoryId == category.id) Color(0xFF6750A4) else Color.Black)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showFilterDialog = false }) { Text("Close") }
                }
            )
        }

        // Delete Confirmation Dialog
        if (transactionToDelete != null) {
            AlertDialog(
                onDismissRequest = { transactionToDelete = null },
                title = { Text("Delete Transaction") },
                text = { Text("Are you sure you want to delete this transaction?") },
                confirmButton = {
                    TextButton(onClick = {
                        transactionToDelete?.let { viewModel.deleteTransaction(it) }
                        transactionToDelete = null
                    }) { Text("Delete", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { transactionToDelete = null }) { Text("Cancel") }
                }
            )
        }
    }
}