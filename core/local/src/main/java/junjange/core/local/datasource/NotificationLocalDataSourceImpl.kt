package junjange.core.local.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import junjange.core.data.datasource.NotificationLocalDataSource
import junjange.core.data.model.local.LuckyLotteryNotificationDto
import javax.inject.Inject

internal class NotificationLocalDataSourceImpl
    @Inject
    constructor(
        private val sharedPreferences: SharedPreferences,
    ) : NotificationLocalDataSource {
        override suspend fun getNotification(): Result<LuckyLotteryNotificationDto> =
            runCatching {
                val isLotteryEnabled = sharedPreferences.getBoolean(KEY_LOTTERY_NOTIFICATION, false)
                val isPensionLotteryEnabled =
                    sharedPreferences.getBoolean(KEY_PENSION_LOTTERY_NOTIFICATION, false)

                LuckyLotteryNotificationDto(
                    lotteryNotification = isLotteryEnabled,
                    pensionLotteryNotification = isPensionLotteryEnabled,
                )
            }

        override suspend fun saveLotteryNotification(isEnabled: Boolean): Result<Unit> =
            runCatching {
                sharedPreferences.edit {
                    putBoolean(KEY_LOTTERY_NOTIFICATION, isEnabled)
                }
            }

        override suspend fun savePensionLotteryNotification(isEnabled: Boolean): Result<Unit> =
            runCatching {
                sharedPreferences.edit {
                    putBoolean(KEY_PENSION_LOTTERY_NOTIFICATION, isEnabled)
                }
            }

        private companion object {
            const val KEY_LOTTERY_NOTIFICATION = "lottery_notification_enabled"
            const val KEY_PENSION_LOTTERY_NOTIFICATION = "pension_lottery_notification_enabled"
        }
    }
