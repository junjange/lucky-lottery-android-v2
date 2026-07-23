package junjange.core.remote.di

import junjange.core.data.datasource.LotteryDataSource
import junjange.core.remote.datasource.LotteryDataSourceImpl
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single<LotteryDataSource> { LotteryDataSourceImpl(lotteryService = get()) }
}
