package com.example.voltwatch.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlin.math.min

@Composable
fun BatteryIndicator(level: Int) {

    // 🔥 Smooth animation
    val animatedProgress by animateFloatAsState(
        targetValue = level / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "battery_anim"
    )

    // 🎨 Dynamic gradient colors
    val gradientColors = when {
        level > 70 -> listOf(Color(0xFF00E676), Color(0xFF00C853)) // Green
        level > 30 -> listOf(Color(0xFFFFD600), Color(0xFFFFA000)) // Yellow
        else -> listOf(Color(0xFFFF5252), Color(0xFFD50000))       // Red
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(220.dp)
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            val strokeWidth = 18.dp.toPx()
            val radius = (min(size.width, size.height) - strokeWidth) / 2

            // ⚪ Background circle
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.2f),
                radius = radius,
                style = Stroke(width = strokeWidth)
            )

            // 🌈 Gradient progress arc
            drawArc(
                brush = Brush.sweepGradient(gradientColors),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )

            // ✨ Glow effect
            drawCircle(
                brush = Brush.radialGradient(
                    colors = gradientColors,
                    center = center,
                    radius = radius
                ),
                radius = radius,
                alpha = 0.12f
            )
        }

        // 🔢 Center Text
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "$level%",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Battery",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}