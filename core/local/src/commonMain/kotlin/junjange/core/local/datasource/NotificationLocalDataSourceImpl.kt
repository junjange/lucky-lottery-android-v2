package junjange.core.local.datasource

import com.russhwolf.settings.Settings
import junjange.core.data.datasource.NotificationLocalDataSource
import junjange.core.data.model.local.LuckyLotteryNotificationDto
import junjange.core.local.notification.NotificationScheduler

internal class NotificationLocalDataSourceImpl(
    private val settings: Settings,
    private val scheduler: NotificationScheduler,
) : NotificationLocalDataSource {
    override suspend fun getNotification(): Result<LuckyLotteryNotificationDto> =
        runCatching {
            LuckyLotteryNotificationDto(
                lotteryNotification = settings.getBoolean(KEY_LOTTERY_NOTIFICATION, false),
                pensionLotteryNotification = settings.getBoolean(KEY_PENSION_LOTTERY_NOTIFICATION, false),
            )
        }

    override suspend fun saveLotteryNotification(isEnabled: Boolean): Result<Unit> =
        runCatching {
            settings.putBoolean(KEY_LOTTERY_NOTIFICATION, isEnabled)
            if (isEnabled) scheduler.scheduleLottoNotification() else scheduler.cancelLottoNotification()
        }

    override suspend fun savePensionLotteryNotification(isEnabled: Boolean): Result<Unit> =
        runCatching {
            settings.putBoolean(KEY_PENSION_LOTTERY_NOTIFICATION, isEnabled)
            if (isEnabled) scheduler.schedulePensionLottoNotification() else scheduler.cancelPensionLottoNotification()
        }

    private companion object {
        const val KEY_LOTTERY_NOTIFICATION = "lottery_notification_enabled"
        const val KEY_PENSION_LOTTERY_NOTIFICATION = "pension_lottery_notification_enabled"
    }
}
