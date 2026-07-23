package junjange.feature.setting

sealed class SettingEffect {
    class NavigateToNotification(
        val lottoNotificationState: Boolean,
        val pensionLottoNotificationState: Boolean,
    ) : SettingEffect()

    data object NavigateToUsageTerm : SettingEffect()

    data object NavigateToReview : SettingEffect()
}
