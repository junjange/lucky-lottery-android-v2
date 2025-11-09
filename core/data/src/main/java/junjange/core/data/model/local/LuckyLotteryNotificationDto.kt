package junjange.core.data.model.local

import junjange.core.domain.model.LuckyLotteryNotification

data class LuckyLotteryNotificationDto(
    val lotteryNotification: Boolean,
    val pensionLotteryNotification: Boolean,
) {
    fun toDomain() =
        LuckyLotteryNotification(
            lotteryNotification = lotteryNotification,
            pensionLotteryNotification = pensionLotteryNotification,
        )
}
