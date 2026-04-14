package com.example.voltwatch.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomBar(nav: NavController) {

    val currentRoute = nav.currentBackStackEntry?.destination?.route

    NavigationBar {

        // 🏠 Dashboard
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = {
                nav.navigate("dashboard") {
                    popUpTo("dashboard")
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Dashboard"
                )
            },
            label = { Text("Home") }
        )

        // 📊 History
        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = {
                nav.navigate("history") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.Default.History,
                    contentDescription = "History"
                )
            },
            label = { Text("History") }
        )

        // 🔔 Alert
        NavigationBarItem(
            selected = currentRoute == "alert",
            onClick = {
                nav.navigate("alert") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Alert"
                )
            },
            label = { Text("Alert") }
        )
    }
}