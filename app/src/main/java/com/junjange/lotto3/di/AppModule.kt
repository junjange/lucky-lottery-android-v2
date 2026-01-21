package com.junjange.lotto3.di

import android.content.SharedPreferences
import androidx.core.content.edit
import com.junjange.lotto3.BuildConfig
import junjange.core.remote.api.AuthenticationListener
import junjange.core.remote.api.BaseUrl
import junjange.core.remote.interceptor.Interceptors
import junjange.feature.login.LoginActivity
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { BaseUrl(BuildConfig.BASE_URL) }

    single {
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
            Interceptors(listOf(loggingInterceptor))
        } else {
            Interceptors.Empty
        }
    }

    single<AuthenticationListener> {
        object : AuthenticationListener {
            override fun onSessionExpired() {
                get<SharedPreferences>().edit { clear() }
                LoginActivity.startActivity(context = androidContext())
            }
        }
    }
}
