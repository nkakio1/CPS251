package com.example.asn_final_yujarecrm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asn_final_yujarecrm.Data.FinanceDatabase
import com.example.asn_final_yujarecrm.Nav.Navigation
import com.example.asn_final_yujarecrm.data.FinanceRepository
import com.example.asn_final_yujarecrm.viewmodel.FinanceViewModel
import com.example.asn_final_yujarecrm.viewmodel.FinanceViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = FinanceDatabase.getDatabase(applicationContext)

        // CHANGED: Initialize Repository using the DAOs
        val repository = FinanceRepository(database.transactionDao(), database.categoryDao())

        // CHANGED: Pass Repository to the Factory
        val viewModelFactory = FinanceViewModelFactory(repository)

        setContent {
            val viewModel: FinanceViewModel = viewModel(factory = viewModelFactory)
            Navigation(viewModel = viewModel)
        }
    }
    /*
    Why is FinanceRepository used instead of calling DAOs directly from the ViewModel?
    What problem does this solve?

    Why is FinanceViewModelFactory needed?
    Why can't FinanceViewModel be instantiated directly in MainActivity?

    In TransactionListScreen, how does the filter dialog update the
     displayed transactions? Trace the data
     flow from user selection to UI update.

    When navigating from Home → TransactionList →
    AddEditTransaction, what happens to the back stack?
    How does popBackStack() work?

    Why are repository operations wrapped in viewModelScope.launch?
     What would happen if they were called directly
    without a coroutine scope?
     */
}
