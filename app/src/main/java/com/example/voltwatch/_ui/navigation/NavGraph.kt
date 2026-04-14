package com.example.voltwatch._ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voltwatch.ui.alert.AlertScreen
import com.example.voltwatch.ui.dashboard.DashboardScreen
import com.example.voltwatch.ui.history.HistoryScreen
import com.example.voltwatch.viewmodel.BatteryViewModel

@Composable
fun NavGraph(vm: BatteryViewModel) {

    val navController = rememberNavController()

    NavHost(navController, "dashboard") {
        composable("dashboard") { DashboardScreen(vm, navController) }
        composable("history") { HistoryScreen(vm, navController) }
        composable("alert") { AlertScreen(vm, navController) }
    }
}
