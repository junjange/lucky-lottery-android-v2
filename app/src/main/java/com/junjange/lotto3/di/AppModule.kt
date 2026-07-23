package com.junjange.lotto3.di

import junjange.core.notification.NotificationConfig
import junjange.feature.main.MainActivity
import org.koin.dsl.module

val appModule = module {
    single {
        NotificationConfig(
            mainActivityClass = MainActivity::class.java,
            appIconRes = junjange.feature.main.R.drawable.app_icon,
        )
    }
}
