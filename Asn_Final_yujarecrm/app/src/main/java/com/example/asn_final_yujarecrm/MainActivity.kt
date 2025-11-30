package com.example.asn_final_yujarecrm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.asn_final_yujarecrm.ViewModel.FinanceViewModel
import com.example.asn_final_yujarecrm.ViewModel.FinanceViewModelFactory
import com.example.asn_final_yujarecrm.screens.AddEditTransactionScreen
import com.example.finalproject.data.FinanceDatabase
import com.example.asn_final_yujarecrm.screens.HomeScreen
import com.example.asn_final_yujarecrm.screens.CategoryManagementScreen
import com.example.finalproject.ui.TransactionListScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = FinanceDatabase.getDatabase(applicationContext)
        val viewModelFactory = FinanceViewModelFactory(database)

        setContent {
            val navController = rememberNavController()
            val viewModel: FinanceViewModel = viewModel(factory = viewModelFactory)

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(viewModel, navController)
                }

                composable("categories") {
                    CategoryManagementScreen(viewModel, navController)
                }

                composable("transaction_list") {
                    TransactionListScreen(viewModel, navController)
                }

                composable("add_transaction") {
                    AddEditTransactionScreen(viewModel, navController)
                }

                composable(
                    route = "edit_transaction/{transactionId}",
                    arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val transactionId = backStackEntry.arguments?.getLong("transactionId")
                    AddEditTransactionScreen(viewModel, navController, transactionId)
                }
            }
        }
    }
}