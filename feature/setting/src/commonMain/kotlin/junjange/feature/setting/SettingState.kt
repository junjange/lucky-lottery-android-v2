package junjange.feature.setting

data class SettingState(
    val isLoading: Boolean = false,
    val isNotificationAvailable: Boolean = false,
    val lotteryNotificationStatus: Boolean = false,
    val pensionLotteryNotificationStatus: Boolean = false,
)
