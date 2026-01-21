package com.junjange.lotto3.navigation

import android.content.Context
import junjange.core.navigation.RegisterNavigator
import junjange.feature.main.MainActivity

class RegisterNavigatorImpl : RegisterNavigator {
    override fun startMainActivity(context: Context) {
        MainActivity.startActivity(context = context)
    }
}
