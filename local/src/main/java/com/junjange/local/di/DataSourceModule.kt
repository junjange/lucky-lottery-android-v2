package com.junjange.local.di

import com.junjange.data.datasource.LocalDataSource
import com.junjange.data.datasource.LotteryRoomDataSource
import com.junjange.data.datasource.PensionLotteryRoomDataSource
import com.junjange.local.datasource.LocalDataSourceImpl
import com.junjange.local.datasource.LotteryRoomDataSourceImpl
import com.junjange.local.datasource.PensionLotteryRoomDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    abstract fun bindsLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun bindsLotteryRoomDataSource(lotteryRoomDataSourceImpl: LotteryRoomDataSourceImpl): LotteryRoomDataSource

    @Binds
    abstract fun bindsPensionLotteryRoomDataSource(
        pensionLotteryRoomDataSourceImpl: PensionLotteryRoomDataSourceImpl,
    ): PensionLotteryRoomDataSource
}
