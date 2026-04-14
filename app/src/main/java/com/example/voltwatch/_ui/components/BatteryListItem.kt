package com.example.voltwatch._ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
 import com.example.voltwatch.data.datastore.BatteryRecord

@Composable
fun BatteryListItem(record: BatteryRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "${record.percentage}%",
            color = Color(0xFF2E7D32)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "- Recorded: ${record.time}")
    }
}
