package com.example.asn_final_yujarecrm.Nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.asn_final_yujarecrm.screens.AddEditTransactionScreen
import com.example.asn_final_yujarecrm.screens.CategoryManagementScreen
import com.example.asn_final_yujarecrm.screens.HomeScreen
import com.example.asn_final_yujarecrm.screens.TransactionListScreen
import com.example.asn_final_yujarecrm.viewmodel.FinanceViewModel

@Composable
fun Navigation(viewModel: FinanceViewModel) {
    val navController = rememberNavController()

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