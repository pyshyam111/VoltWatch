package com.example.voltwatch.domain.usecase

import com.example.voltwatch.data.repository.BatteryRepository

class SaveBatteryLogUseCase(private val repo: BatteryRepository) {
    suspend operator fun invoke(level: Int) {
        repo.insert(level)
    }
}