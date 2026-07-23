package junjange.core.domain.repository

import junjange.core.domain.model.LuckyLotteryNotification

interface UserRepository {
    suspend fun patchLotteryNotification(notificationStatus: Boolean): Result<Unit>

    suspend fun patchPensionLotteryNotification(notificationStatus: Boolean): Result<Unit>

    suspend fun getNotification(): Result<LuckyLotteryNotification>
}
