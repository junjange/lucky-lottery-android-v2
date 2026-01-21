package com.junjange.lotto3.di

import android.content.SharedPreferences
import androidx.core.content.edit
import com.junjange.lotto3.BuildConfig
import junjange.core.remote.api.AuthenticationListener
import junjange.core.remote.api.BaseUrl
import junjange.feature.login.LoginActivity
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { BaseUrl(BuildConfig.BASE_URL) }

    single<AuthenticationListener> {
        object : AuthenticationListener {
            override fun onSessionExpired() {
                get<SharedPreferences>().edit { clear() }
                LoginActivity.startActivity(context = androidContext())
            }
        }
    }
}
