package junjange.feature.setting

sealed class SettingEffect {
    data object NavigateToUsageTerm : SettingEffect()

    data object NavigateToReview : SettingEffect()
}
