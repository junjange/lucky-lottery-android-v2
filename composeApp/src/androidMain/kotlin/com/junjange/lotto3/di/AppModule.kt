package com.junjange.lotto3.di

import com.junjange.lotto3.MainActivity
import com.junjange.lotto3.R
import junjange.core.notification.NotificationConfig
import org.koin.dsl.module

val appModule = module {
    single {
        NotificationConfig(
            mainActivityClass = MainActivity::class.java,
            appIconRes = R.drawable.app_icon,
        )
    }
}
