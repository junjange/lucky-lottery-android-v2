package junjange.core.remote.di

import junjange.core.data.datasource.CredentialDataSource
import junjange.core.data.datasource.ImagesDataSource
import junjange.core.data.datasource.LotteryDataSource
import junjange.core.data.datasource.NotificationDataSource
import junjange.core.data.datasource.PensionLotteryDataSource
import junjange.core.data.datasource.UserDataSource
import junjange.core.data.datasource.WinningDataSource
import junjange.core.remote.datasource.CredentialDataSourceImpl
import junjange.core.remote.datasource.ImagesDataSourceImpl
import junjange.core.remote.datasource.LotteryDataSourceImpl
import junjange.core.remote.datasource.NotificationDataSourceImpl
import junjange.core.remote.datasource.PensionLotteryDataSourceImpl
import junjange.core.remote.datasource.UserDataSourceImpl
import junjange.core.remote.datasource.WinningDataSourceImpl
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single<CredentialDataSource> { CredentialDataSourceImpl(apiService = get()) }
    single<UserDataSource> { UserDataSourceImpl(apiService = get()) }
    single<LotteryDataSource> { LotteryDataSourceImpl(apiService = get(), lotteryService = get()) }
    single<PensionLotteryDataSource> { PensionLotteryDataSourceImpl(apiService = get()) }
    single<WinningDataSource> { WinningDataSourceImpl(apiService = get()) }
    single<NotificationDataSource> { NotificationDataSourceImpl(apiService = get()) }
    single<ImagesDataSource> { ImagesDataSourceImpl(apiService = get()) }
}
