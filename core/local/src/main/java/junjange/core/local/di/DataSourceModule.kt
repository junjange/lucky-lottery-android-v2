package junjange.core.local.di

import junjange.core.data.datasource.LocalDataSource
import junjange.core.data.datasource.LotteryRoomDataSource
import junjange.core.data.datasource.NotificationLocalDataSource
import junjange.core.data.datasource.PensionLotteryRoomDataSource
import junjange.core.local.datasource.LocalDataSourceImpl
import junjange.core.local.datasource.LotteryRoomDataSourceImpl
import junjange.core.local.datasource.NotificationLocalDataSourceImpl
import junjange.core.local.datasource.PensionLotteryRoomDataSourceImpl
import org.koin.dsl.module

val localDataSourceModule = module {
    single<LocalDataSource> { LocalDataSourceImpl(sharedPreferences = get()) }
    single<LotteryRoomDataSource> { LotteryRoomDataSourceImpl(dao = get()) }
    single<PensionLotteryRoomDataSource> { PensionLotteryRoomDataSourceImpl(dao = get()) }
    single<NotificationLocalDataSource> { NotificationLocalDataSourceImpl(sharedPreferences = get(), workManager = get()) }
}
