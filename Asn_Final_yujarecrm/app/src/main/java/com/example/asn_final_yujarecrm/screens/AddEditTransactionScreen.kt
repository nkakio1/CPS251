package com.example.asn_final_yujarecrm.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.asn_final_yujarecrm.ViewModel.FinanceViewModel
import com.example.finalproject.data.Transaction
import com.example.finalproject.data.TransactionType
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditTransactionScreen(
    viewModel: FinanceViewModel,
    navController: NavController,
    transactionId: Long? = null
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val categories by viewModel.allCategories.collectAsState()

    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var date by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Logic: Reset category selection when switching types (e.g. Expense -> Income)
    LaunchedEffect(selectedType) {
        if (transactionId == null) {
            selectedCategoryId = null
        }
    }

    LaunchedEffect(transactionId) {
        if (transactionId != null) {
            val transaction = viewModel.getTransactionById(transactionId)
            transaction?.let {
                description = it.description
                amount = it.amount.toString()
                selectedType = it.type
                selectedCategoryId = it.categoryId
                date = it.date
            }
        }
    }

    // Shared Save Function
    fun saveTransaction() {
        if (description.isBlank() || amount.isBlank() || selectedCategoryId == null) {
            Toast.makeText(context, "Please select a category and fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            val transaction = Transaction(
                id = transactionId ?: 0,
                amount = amount.toDouble(),
                date = date,
                description = description,
                categoryId = selectedCategoryId!!,
                type = selectedType
            )
            scope.launch {
                if (transactionId == null) {
                    viewModel.addTransaction(transaction)
                } else {
                    viewModel.updateTransaction(transaction)
                }
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (transactionId == null) "Add Transaction" else "Edit Transaction",
                        color = Color(0xFF1D1B20)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Top Bar Save Button now works
                    TextButton(onClick = { saveTransaction() }) {
                        Text("Save", color = Color(0xFF6750A4))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8DEF8))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Type Selection (Custom Toggle Row)
            Text("Type", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Expense Button
                Button(
                    onClick = { selectedType = TransactionType.EXPENSE },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == TransactionType.EXPENSE) Color(0xFFE8DEF8) else Color.Transparent,
                        contentColor = if (selectedType == TransactionType.EXPENSE) Color(0xFF1D1B20) else Color.Gray
                    ),
                    border = if (selectedType != TransactionType.EXPENSE) BorderStroke(1.dp, Color.Gray) else null
                ) {
                    if (selectedType == TransactionType.EXPENSE) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text("Expense")
                }

                // Income Button
                Button(
                    onClick = { selectedType = TransactionType.INCOME },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedType == TransactionType.INCOME) Color(0xFFE8DEF8) else Color.Transparent,
                        contentColor = if (selectedType == TransactionType.INCOME) Color(0xFF1D1B20) else Color.Gray
                    ),
                    border = if (selectedType != TransactionType.INCOME) BorderStroke(1.dp, Color.Gray) else null
                ) {
                    if (selectedType == TransactionType.INCOME) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text("Income")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection
            Text("Category", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            val filteredCategories = categories.filter { it.type == selectedType }

            // FIX: Show warning if no categories exist for the selected type
            if (filteredCategories.isEmpty()) {
                Text(
                    text = "No ${selectedType.name.lowercase()} categories found. Please add one in 'Manage Categories'.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    filteredCategories.forEach { category ->
                        FilterChip(
                            selected = selectedCategoryId == category.id,
                            onClick = { selectedCategoryId = category.id },
                            label = { Text(category.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE8DEF8),
                                selectedLabelColor = Color(0xFF1D1B20)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inputs
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date Picker (Styled as gray box)
            Text("Date", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                    .clickable { showDatePicker = true }
                    .padding(16.dp)
            ) {
                Text(Date(date).toString())
            }

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date)
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            date = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                            showDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bottom Save Button
            Button(
                onClick = { saveTransaction() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
            ) {
                Text("Save Transaction")
            }
        }
    }
}