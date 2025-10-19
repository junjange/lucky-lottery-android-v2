package com.junjange.lotto3.navigation

import android.content.Context
import junjange.core.domain.model.OauthProvider
import junjange.core.navigation.MainNavigator
import junjange.feature.editprofile.EditProfileActivity
import junjange.feature.login.LoginActivity
import junjange.feature.notification.NotificationActivity
import junjange.feature.randomnumber.RandomNumberActivity
import junjange.feature.withdrawal.WithdrawalActivity
import javax.inject.Inject

class MainNavigatorImpl
    @Inject
    constructor() : MainNavigator {
        override fun startRandomActivity(context: Context) {
            RandomNumberActivity.startActivity(context = context)
        }

        override fun startEditProfileActivity(
            context: Context,
            nickname: String,
            profilePath: String?,
        ) {
            EditProfileActivity.startActivity(
                context = context,
                nickname = nickname,
                profilePath = profilePath,
            )
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

        override fun startWithdrawalActivity(
            context: Context,
            oauthProvider: OauthProvider,
        ) {
            WithdrawalActivity.startActivity(
                context = context,
                oauthProvider = oauthProvider,
            )
        }

        override fun startLoginActivity(context: Context) {
            LoginActivity.startActivity(context = context)
        }
    }
