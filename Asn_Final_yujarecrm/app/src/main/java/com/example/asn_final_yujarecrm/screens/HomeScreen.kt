package com.example.asn_final_yujarecrm.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.asn_final_yujarecrm.ViewModel.FinanceViewModel
import com.example.finalproject.data.Transaction
import com.example.finalproject.data.TransactionType
import java.text.NumberFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: FinanceViewModel, navController: NavController) {
    val balance by viewModel.currentBalance.collectAsState()
    val income by viewModel.totalIncome.collectAsState()
    val expense by viewModel.totalExpense.collectAsState()
    val recentTransactions by viewModel.allTransactions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finance Tracker", color = Color(0xFF1D1B20)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8DEF8))
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Balance Card
            BalanceCard(balance)

            Spacer(modifier = Modifier.height(16.dp))

            // Income/Expense Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Matches screenshot: Green background
                SummaryCard(
                    title = "Income",
                    amount = income,
                    backgroundColor = Color(0xFF4CAF50),
                    textColor = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                // Matches screenshot: Red/Salmon background
                SummaryCard(
                    title = "Expense",
                    amount = expense,
                    backgroundColor = Color(0xFFF44336),
                    textColor = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions
            Text("Quick Actions", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ActionButton("Transactions", onClick = { navController.navigate("transaction_list") }, modifier = Modifier.weight(1f))
                ActionButton("Categories", onClick = { navController.navigate("categories") }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Transactions Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Transactions (most recent 5)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                TextButton(onClick = { navController.navigate("transaction_list") }) {
                    Text("View All", fontSize = 12.sp)
                }
            }

            // List
            LazyColumn {
                items(recentTransactions.take(5)) { transaction ->
                    // Home screen items don't have delete button in screenshot
                    TransactionItem(
                        transaction = transaction,
                        showDelete = false,
                        onClick = { navController.navigate("edit_transaction/${transaction.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun BalanceCard(balance: Double) {
    val isPositive = balance >= 0
    // Screenshot colors: Positive = Light Purple, Negative = Light Pink
    val containerColor = if (isPositive) Color(0xFFE8DEF8) else Color(0xFFFFDAD6)
    // The requirement was for text to NOT be red, so we force Black here
    val contentColor = Color.Black

    val icon = if (isPositive) Icons.Default.CheckCircle else Icons.Default.Warning
    val iconTint = if (isPositive) Color(0xFF6750A4) else Color.Black

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Current Balance", style = MaterialTheme.typography.bodySmall, color = Color.Black)
            Text(
                text = NumberFormat.getCurrencyInstance().format(balance),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor // This is now forced to Black
            )
        }
    }
}

@Composable
fun SummaryCard(title: String, amount: Double, backgroundColor: Color, textColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = textColor)
            Text(
                text = NumberFormat.getCurrencyInstance().format(amount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8DEF8), contentColor = Color.Black),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

// Reusable item for both screens
@Composable
fun TransactionItem(
    transaction: Transaction,
    showDelete: Boolean = false,
    onDelete: () -> Unit = {},
    onClick: () -> Unit
) {
    val amountColor = if (transaction.type == TransactionType.INCOME) Color(0xFF4CAF50) else Color(0xFFF44336)

    // LOGIC CHANGE: If it is an EXPENSE, make the number negative so it shows "-$..."
    val finalAmount = if (transaction.type == TransactionType.EXPENSE) {
        -transaction.amount
    } else {
        transaction.amount
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(
                Date(transaction.date).toString().take(10),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = NumberFormat.getCurrencyInstance().format(finalAmount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            if (showDelete) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFB3261E)
                    )
                }
            }
        }
    }
}