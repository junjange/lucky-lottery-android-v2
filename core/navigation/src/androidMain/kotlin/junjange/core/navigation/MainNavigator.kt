package junjange.core.navigation

import android.content.Context

interface MainNavigator {
    fun startRandomActivity(context: Context)

    fun startNotificationActivity(
        context: Context,
        lottoNotificationState: Boolean,
        pensionLottoNotificationState: Boolean,
    )
}
