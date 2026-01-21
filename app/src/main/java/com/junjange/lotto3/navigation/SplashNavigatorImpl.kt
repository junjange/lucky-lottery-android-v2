package com.junjange.lotto3.navigation

import android.content.Context
import junjange.core.domain.model.OauthProvider
import junjange.core.navigation.MainNavigator
import junjange.core.navigation.SplashNavigator
import junjange.feature.editprofile.EditProfileActivity
import junjange.feature.login.LoginActivity
import junjange.feature.main.MainActivity
import junjange.feature.notification.NotificationActivity
import junjange.feature.randomnumber.RandomNumberActivity
import junjange.feature.withdrawal.WithdrawalActivity

class SplashNavigatorImpl : SplashNavigator {
        override fun startMainActivity(context: Context) {
            MainActivity.startActivity(context = context)
        }

        override fun startLoginActivity(context: Context) {
            LoginActivity.startActivity(context = context)
        }
    }
