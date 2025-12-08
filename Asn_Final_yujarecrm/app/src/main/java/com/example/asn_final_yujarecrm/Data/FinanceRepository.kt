package com.example.asn_final_yujarecrm.data

import com.example.asn_final_yujarecrm.Data.Category
import com.example.asn_final_yujarecrm.Data.CategoryDao
import com.example.asn_final_yujarecrm.Data.Transaction
import com.example.asn_final_yujarecrm.Data.TransactionDao
import kotlinx.coroutines.flow.Flow

class FinanceRepository(private val transactionDao: TransactionDao, private val categoryDao: CategoryDao) {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: Transaction) = transactionDao.insertTransaction(transaction)
    suspend fun updateTransaction(transaction: Transaction) = transactionDao.updateTransaction(transaction)
    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.deleteTransaction(transaction)
    suspend fun getTransactionById(id: Long): Transaction? = transactionDao.getTransactionById(id)

    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
}