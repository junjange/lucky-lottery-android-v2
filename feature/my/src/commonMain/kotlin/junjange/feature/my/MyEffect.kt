package junjange.feature.my

import junjange.core.domain.model.OauthProvider

sealed class MyEffect {
    class NavigateToEditProfile(
        val nickname: String,
        val profilePath: String?,
    ) : MyEffect()

    data object NavigateToUsageTerm : MyEffect()

    data object NavigateToSplash : MyEffect()

    class NavigateToWithdrawal(val oauthProvider: OauthProvider) : MyEffect()

    class NavigateToNotification(
        val lottoNotificationState: Boolean,
        val pensionLottoNotificationState: Boolean,
    ) : MyEffect()
}
