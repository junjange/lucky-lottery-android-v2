package junjange.core.local.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import junjange.core.local.dao.LotteryDao
import junjange.core.local.dao.PensionLotteryDao
import junjange.core.local.room.LotteryDatabase
import junjange.core.local.room.PensionLotteryDatabase
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
