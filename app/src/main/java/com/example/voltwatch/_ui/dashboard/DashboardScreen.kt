package com.example.voltwatch.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.voltwatch.viewmodel.BatteryViewModel

@Composable
fun DashboardScreen(vm: BatteryViewModel, nav: NavController) {

    val battery by vm.battery.collectAsState()
    val insights by vm.batteryInsights.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(nav) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔋 PREMIUM BATTERY
            PremiumBatteryIndicator(battery.level)

            Spacer(Modifier.height(20.dp))

            // 🔥 Temp + Voltage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallInfoCard(
                    modifier = Modifier.weight(1f),
                    title = "Temperature",
                    value = "${battery.temp}°C",
                    icon = Icons.Default.Thermostat
                )

                SmallInfoCard(
                    modifier = Modifier.weight(1f),
                    title = "Voltage",
                    value = "${battery.voltage} V",
                    icon = Icons.Default.BatteryChargingFull
                )
            }

            Spacer(Modifier.height(12.dp))

            // 🔋 Technology
            LargeInfoCard("Technology", battery.tech)

            Spacer(Modifier.height(12.dp))

            // ⚡ Status (UPDATED)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BlueCard(
                    modifier = Modifier.weight(1f),
                    title = "Remaining",
                    value = battery.remainingTime
                )

                BlueCard(
                    modifier = Modifier.weight(1f),
                    title = "Status",
                    value = battery.status
                )
            }

            Spacer(Modifier.height(20.dp))

            // 🤖 AI SMART INSIGHTS
            Text(
                "Smart Insights",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            if (insights.isEmpty()) {
                Text(
                    "Analyzing battery usage...",
                    color = Color.Gray
                )
            } else {
                insights.forEach { tip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = tip,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🔔 ALERT BUTTON
            Button(
                onClick = {
                    nav.navigate("alert") { launchSingleTop = true }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Set Battery Alert")
            }
        }
    }
}


@Composable
fun BlueCard(modifier: Modifier, title: String, value: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color.White)
            Text(value, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LargeInfoCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SmallInfoCard(
    modifier: Modifier,
    title: String,
    value: String,
    icon: ImageVector
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null)
            Spacer(Modifier.height(8.dp))
            Text(title)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PremiumBatteryIndicator(level: Int) {

    val color = when {
        level > 70 -> Color(0xFF4CAF50)
        level > 30 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Box(
        modifier = Modifier
            .size(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(color.copy(0.3f), color)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "$level%",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("Battery", color = Color.White)
        }
    }
}

@Composable
fun BottomBar(nav: NavController) {

    val current = nav.currentBackStackEntry?.destination?.route

    NavigationBar {

        NavigationBarItem(
            selected = current == "dashboard",
            onClick = {
                nav.navigate("dashboard") {
                    popUpTo("dashboard")
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = current == "history",
            onClick = {
                nav.navigate("history") { launchSingleTop = true }
            },
            icon = { Icon(Icons.Default.History, null) },
            label = { Text("History") }
        )

        NavigationBarItem(
            selected = current == "alert",
            onClick = {
                nav.navigate("alert") { launchSingleTop = true }
            },
            icon = { Icon(Icons.Default.Notifications, null) },
            label = { Text("Alert") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        title = { Text("VoltWatch", fontWeight = FontWeight.Bold) }
    )
}
