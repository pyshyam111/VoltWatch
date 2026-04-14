package com.example.voltwatch._ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp

@Composable
fun BatteryChart(data: List<Int>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 16.dp)
    ) {
        if (data.size < 2) return@Canvas

        val maxValue = 100f
        val widthStep = size.width / (data.size - 1)
        
        // 🛠️ CREATE BEZIER PATH FOR SMOOTH CURVES
        val points = data.mapIndexed { index, value ->
            Offset(
                x = index * widthStep,
                y = size.height - (value / maxValue) * size.height
            )
        }

        val strokePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 1) {
                val p1 = points[i]
                val p2 = points[i + 1]
                val controlPoint1 = Offset((p1.x + p2.x) / 2f, p1.y)
                val controlPoint2 = Offset((p1.x + p2.x) / 2f, p2.y)
                cubicTo(
                    controlPoint1.x, controlPoint1.y,
                    controlPoint2.x, controlPoint2.y,
                    p2.x, p2.y
                )
            }
        }

        // 🌊 DRAW AREA GRADIENT WITH CLIPPING
        val fillPath = Path().apply {
            addPath(strokePath)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        clipPath(fillPath) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4CAF50).copy(alpha = 0.5f),
                        Color(0xFF4CAF50).copy(alpha = 0.0f)
                    )
                )
            )
        }

        // 📈 DRAW SMOOTH LINE
        drawPath(
            path = strokePath,
            color = Color(0xFF4CAF50),
            style = Stroke(width = 6f)
        )

        // 🔘 DRAW GLOWING POINTS
        points.forEachIndexed { index, point ->
            // Draw outer glow
            drawCircle(
                color = Color(0xFF4CAF50).copy(alpha = 0.3f),
                radius = 12f,
                center = point
            )
            // Draw inner point
            drawCircle(
                color = Color.White,
                radius = 6f,
                center = point
            )
            drawCircle(
                color = Color(0xFF2E7D32),
                radius = 4f,
                center = point
            )
        }
    }
}
