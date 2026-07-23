package junjange.core.local.di

import junjange.core.data.datasource.LocalDataSource
import junjange.core.data.datasource.LotteryRoomDataSource
import junjange.core.data.datasource.NotificationLocalDataSource
import junjange.core.data.datasource.PensionLotteryRoomDataSource
import junjange.core.data.provider.AccessTokenProvider
import junjange.core.data.provider.RefreshTokenProvider
import junjange.core.local.dao.LotteryDao
import junjange.core.local.dao.PensionLotteryDao
import junjange.core.local.database.LotteryDatabase
import junjange.core.local.database.PensionLotteryDatabase
import junjange.core.local.datasource.LotteryRoomDataSourceImpl
import junjange.core.local.datasource.NotificationLocalDataSourceImpl
import junjange.core.local.datasource.PensionLotteryRoomDataSourceImpl
import junjange.core.local.datasource.SettingsLocalDataSource
import junjange.core.local.provider.AccessTokenProviderImpl
import junjange.core.local.provider.RefreshTokenProviderImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val localDataSourceModule = module {
    single<LotteryDao> { get<LotteryDatabase>().lotteryDao() }
    single<PensionLotteryDao> { get<PensionLotteryDatabase>().pensionLotteryDao() }
    single<LocalDataSource> { SettingsLocalDataSource(settings = get()) }
    single<LotteryRoomDataSource> { LotteryRoomDataSourceImpl(dao = get()) }
    single<PensionLotteryRoomDataSource> { PensionLotteryRoomDataSourceImpl(dao = get()) }
    single<NotificationLocalDataSource> {
        NotificationLocalDataSourceImpl(settings = get(), scheduler = get())
    }
}

val providerModule = module {
    single<AccessTokenProvider> { AccessTokenProviderImpl(settings = get()) }
    single<RefreshTokenProvider> { RefreshTokenProviderImpl(settings = get()) }
}

/**
 * Platform-provided dependencies for [localDataSourceModule] and [providerModule]:
 * the [com.russhwolf.settings.Settings] store, the two Room databases, and the
 * [junjange.core.local.notification.NotificationScheduler].
 */
expect val localPlatformModule: Module

/** All local-storage Koin modules, in dependency order. */
val localModules: List<Module>
    get() = listOf(localPlatformModule, localDataSourceModule, providerModule)
