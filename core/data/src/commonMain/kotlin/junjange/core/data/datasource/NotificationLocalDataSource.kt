package junjange.core.data.datasource

import junjange.core.data.model.local.LuckyLotteryNotificationDto

interface NotificationLocalDataSource {
    suspend fun getNotification(): Result<LuckyLotteryNotificationDto>

    suspend fun saveLotteryNotification(isEnabled: Boolean): Result<Unit>

    suspend fun savePensionLotteryNotification(isEnabled: Boolean): Result<Unit>
}
