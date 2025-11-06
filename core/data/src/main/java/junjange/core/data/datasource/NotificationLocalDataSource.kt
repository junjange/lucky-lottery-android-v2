package junjange.core.data.datasource

import junjange.core.data.model.local.LuckyLotteryNotificationDto

/**
 * 알림 설정 상태를 SharedPreferences에 저장/조회하는 로컬 데이터 소스 구현체.
 */
interface NotificationLocalDataSource {
    suspend fun getNotification(): Result<LuckyLotteryNotificationDto>

    suspend fun saveLotteryNotification(isEnabled: Boolean): Result<Unit>

    suspend fun savePensionLotteryNotification(isEnabled: Boolean): Result<Unit>
}
