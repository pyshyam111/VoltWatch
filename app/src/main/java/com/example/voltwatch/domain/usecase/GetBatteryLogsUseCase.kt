package com.example.voltwatch.domain.usecase

import com.example.voltwatch.data.repository.BatteryRepository

class GetBatteryLogsUseCase(private val repo: BatteryRepository) {
    operator fun invoke() = repo.getLogs()
}