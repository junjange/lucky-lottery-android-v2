package com.junjange.lotto3.navigation

import android.content.Context
import junjange.core.navigation.SplashNavigator
import junjange.feature.main.MainActivity

class SplashNavigatorImpl : SplashNavigator {
    override fun startMainActivity(context: Context) {
        MainActivity.startActivity(context = context)
    }
}
