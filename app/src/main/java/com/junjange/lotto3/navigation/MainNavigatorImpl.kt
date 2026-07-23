package com.junjange.lotto3.navigation

import android.content.Context
import junjange.core.navigation.MainNavigator
import junjange.feature.notification.NotificationActivity
import junjange.feature.randomnumber.RandomNumberActivity

class MainNavigatorImpl : MainNavigator {
    override fun startRandomActivity(context: Context) {
        RandomNumberActivity.startActivity(context = context)
    }

    override fun startNotificationActivity(
        context: Context,
        lottoNotificationState: Boolean,
        pensionLottoNotificationState: Boolean,
    ) {
        NotificationActivity.startActivity(
            context = context,
            lottoNotificationState = lottoNotificationState,
            pensionLottoNotificationState = pensionLottoNotificationState,
        )
    }
}
