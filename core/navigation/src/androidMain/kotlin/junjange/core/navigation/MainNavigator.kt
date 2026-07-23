package junjange.core.navigation

import android.content.Context
import junjange.core.domain.model.OauthProvider

interface MainNavigator {
    fun startRandomActivity(context: Context)

    fun startEditProfileActivity(
        context: Context,
        nickname: String,
        profilePath: String?,
    )

    fun startNotificationActivity(
        context: Context,
        lottoNotificationState: Boolean,
        pensionLottoNotificationState: Boolean,
    )

    fun startWithdrawalActivity(
        context: Context,
        oauthProvider: OauthProvider,
    )

    fun startLoginActivity(context: Context)
}
