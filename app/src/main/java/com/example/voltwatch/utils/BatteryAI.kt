package com.example.voltwatch.utils


import com.example.voltwatch.data.local.BatteryEntity

object BatteryAI {

    fun generateInsights(history: List<BatteryEntity>): List<String> {

        if (history.size < 3) return listOf("Not enough data yet")

        val insights = mutableListOf<String>()

        val latest = history.first()
        val old = history.last()

        val diff = old.level - latest.level

        // 🔋 Drain analysis
        if (diff > 15) {
            insights.add("🔋 Battery draining fast")
        } else {
            insights.add("✅ Battery usage is stable")
        }

        // ⚡ Overcharge
        if (latest.level > 90) {
            insights.add("⚡ Avoid charging above 90% regularly")
        }

        //  Low battery habit
        if (latest.level > 0 && latest.level < 100) {
            insights.add("🌡 Device temperature is high, avoid heavy usage")
        }

        return insights
    }
}