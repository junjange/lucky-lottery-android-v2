package com.junjange.lotto3.di

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.junjange.lotto3.BuildConfig
import com.junjange.presentation.ui.login.LoginActivity
import com.junjange.remote.api.AuthenticationListener
import com.junjange.remote.api.BaseUrl
import com.junjange.remote.interceptor.Interceptors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    fun provideBaseUrl(): BaseUrl = BaseUrl(BuildConfig.BASE_URL)

    @Provides
    fun provideInterceptors(): Interceptors =
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY }
            Interceptors(
                listOf(loggingInterceptor),
            )
        } else {
            Interceptors.Empty
        }

    @Provides
    fun provideAuthenticationListener(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences,
    ): AuthenticationListener =
        object : AuthenticationListener {
            override fun onSessionExpired() {
                sharedPreferences.edit { clear() }
                LoginActivity.startActivity(context = context)
            }
        }
}
