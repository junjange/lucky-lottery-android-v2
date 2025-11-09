package junjange.core.notification.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import junjange.core.notification.LottoNotificationManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideLottoNotificationManager(
        @ApplicationContext context: Context,
    ): LottoNotificationManager = LottoNotificationManager(context)
}