package junjange.core.local.di

import junjange.core.data.datasource.LotteryRoomDataSource
import junjange.core.data.datasource.NotificationLocalDataSource
import junjange.core.data.datasource.PensionLotteryRoomDataSource
import junjange.core.local.dao.LotteryDao
import junjange.core.local.dao.PensionLotteryDao
import junjange.core.local.database.LotteryDatabase
import junjange.core.local.database.PensionLotteryDatabase
import junjange.core.local.datasource.LotteryRoomDataSourceImpl
import junjange.core.local.datasource.NotificationLocalDataSourceImpl
import junjange.core.local.datasource.PensionLotteryRoomDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val localDataSourceModule = module {
    single<LotteryDao> { get<LotteryDatabase>().lotteryDao() }
    single<PensionLotteryDao> { get<PensionLotteryDatabase>().pensionLotteryDao() }
    single<LotteryRoomDataSource> { LotteryRoomDataSourceImpl(dao = get()) }
    single<PensionLotteryRoomDataSource> { PensionLotteryRoomDataSourceImpl(dao = get()) }
    single<NotificationLocalDataSource> {
        NotificationLocalDataSourceImpl(settings = get(), scheduler = get())
    }
}

/**
 * Platform-provided dependencies for [localDataSourceModule]:
 * the [com.russhwolf.settings.Settings] store, the two Room databases, and the
 * [junjange.core.local.notification.NotificationScheduler].
 */
expect val localPlatformModule: Module

/** All local-storage Koin modules, in dependency order. */
val localModules: List<Module>
    get() = listOf(localPlatformModule, localDataSourceModule)
