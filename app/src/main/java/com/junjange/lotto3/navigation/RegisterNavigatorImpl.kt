package com.junjange.lotto3.navigation

import android.content.Context
import junjange.core.navigation.RegisterNavigator
import junjange.feature.main.MainActivity
import javax.inject.Inject

class RegisterNavigatorImpl
    @Inject
    constructor() : RegisterNavigator {
        override fun startMainActivity(context: Context) {
            MainActivity.startActivity(context = context)
        }
    }
