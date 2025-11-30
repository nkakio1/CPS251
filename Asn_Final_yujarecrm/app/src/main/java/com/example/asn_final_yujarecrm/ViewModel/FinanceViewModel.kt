package com.example.asn_final_yujarecrm.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asn_final_yujarecrm.Data.Category
import com.example.asn_final_yujarecrm.Data.CategoryDao
import com.example.asn_final_yujarecrm.Data.FinanceDatabase
import com.example.asn_final_yujarecrm.Data.Transaction
import com.example.asn_final_yujarecrm.Data.TransactionDao
import com.example.asn_final_yujarecrm.Data.TransactionType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FinanceViewModel(private val transactionDao: TransactionDao, private val categoryDao: CategoryDao) : ViewModel() {

    // UI State for transactions and categories
    val allTransactions: StateFlow<List<Transaction>> = transactionDao.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategories: StateFlow<List<Category>> = categoryDao.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calculated Totals
    val totalIncome: StateFlow<Double> = allTransactions.map { list ->
        list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense: StateFlow<Double> = allTransactions.map { list ->
        list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val currentBalance: StateFlow<Double> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Database Actions
    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionDao.insertTransaction(transaction)
    }

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionDao.updateTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionDao.deleteTransaction(transaction)
    }

    fun addCategory(category: Category) = viewModelScope.launch {
        categoryDao.insertCategory(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        categoryDao.deleteCategory(category)
    }

    suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)
    }
}

class FinanceViewModelFactory(private val database: FinanceDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinanceViewModel(database.transactionDao(), database.categoryDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}