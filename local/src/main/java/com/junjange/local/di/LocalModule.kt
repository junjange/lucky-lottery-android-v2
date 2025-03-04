package com.junjange.local.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.junjange.local.dao.LotteryDao
import com.junjange.local.dao.PensionLotteryDao
import com.junjange.local.room.LotteryDatabase
import com.junjange.local.room.PensionLotteryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalModule {
    @Provides
    @Singleton
    fun providesLotteryDatabase(
        @ApplicationContext appContext: Context,
    ): LotteryDatabase =
        Room
            .databaseBuilder(
                appContext,
                LotteryDatabase::class.java,
                "lottery_database",
            ).build()

    @Provides
    @Singleton
    fun providesLotteryDao(database: LotteryDatabase): LotteryDao = database.lotteryDao()

    @Provides
    @Singleton
    fun providesPensionLotteryDatabase(
        @ApplicationContext appContext: Context,
    ): PensionLotteryDatabase =
        Room
            .databaseBuilder(
                appContext,
                PensionLotteryDatabase::class.java,
                "pension_lottery_database",
            ).build()

    @Provides
    @Singleton
    fun providesPensionLotteryDao(database: PensionLotteryDatabase): PensionLotteryDao = database.pensionLotteryDao()

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}
