package com.example.voltwatch.ui.alert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.voltwatch.viewmodel.BatteryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(
    vm: BatteryViewModel,
    nav: NavController
) {

    var value by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val currentTarget by vm.target.collectAsState()
    val scope = rememberCoroutineScope()

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF576D9B),
            Color(0xFF264760)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text("Battery Alert", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 🔔 Current Status Chip
                if (currentTarget != -1) {
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Active Alert: $currentTarget%", color = Color.White) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color.White.copy(alpha = 0.2f)
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            borderColor = Color(0xFF4CAF50),
                            enabled = true
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // 🔥 Glass Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Set Target Level 🔋",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Receive notification when battery drops to this level",
                            fontSize = 12.sp,
                            color = Color.White.copy(0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                        )

                        OutlinedTextField(
                            value = value,
                            onValueChange = { if (it.length <= 3) value = it },
                            label = { Text("Enter % (1-100)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // ✅ Numbers only
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF4CAF50),
                                unfocusedBorderColor = Color.Gray.copy(0.5f),
                                focusedLabelColor = Color(0xFF4CAF50),
                                unfocusedLabelColor = Color.White.copy(0.7f),
                                cursorColor = Color.White
                            )
                        )

                        Spacer(Modifier.height(24.dp))

                        // 🔵 PREMIUM BUTTON
                        Button(
                            onClick = {
                                val intValue = value.toIntOrNull()

                                if (intValue == null || intValue !in 1..100) {
                                    message = "❌ Enter valid number (1-100)"
                                } else {
                                    vm.saveTarget(intValue)
                                    message = "✅ Alert set at $intValue%"
                                    
                                    // Auto-hide message and go back after success
                                    scope.launch {
                                        delay(1500)
                                        nav.popBackStack()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            elevation = ButtonDefaults.buttonElevation(8.dp)
                        ) {
                            Text(
                                "Save Alert",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // 🔔 Feedback Message
                        if (message.isNotEmpty()) {
                            Spacer(Modifier.height(16.dp))
                            val isSuccess = message.contains("✅")
                            Text(
                                text = message,
                                color = if (isSuccess) Color(0xFF4CAF50) else Color.Red,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
