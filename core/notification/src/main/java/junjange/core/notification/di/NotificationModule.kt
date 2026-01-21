package junjange.core.notification.di

import junjange.core.notification.LottoNotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val notificationModule = module {
    single { LottoNotificationManager(androidContext()) }
}