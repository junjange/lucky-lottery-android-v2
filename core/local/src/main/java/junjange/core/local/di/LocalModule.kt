package junjange.core.local.di

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import junjange.core.local.dao.LotteryDao
import junjange.core.local.dao.PensionLotteryDao
import junjange.core.local.room.LotteryDatabase
import junjange.core.local.room.PensionLotteryDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            LotteryDatabase::class.java,
            "lottery_database",
        ).build()
    }

    single<LotteryDao> { get<LotteryDatabase>().lotteryDao() }

    single {
        Room.databaseBuilder(
            androidContext(),
            PensionLotteryDatabase::class.java,
            "pension_lottery_database",
        ).build()
    }

    single<PensionLotteryDao> { get<PensionLotteryDatabase>().pensionLotteryDao() }

    single<SharedPreferences> {
        PreferenceManager.getDefaultSharedPreferences(androidContext())
    }
}
