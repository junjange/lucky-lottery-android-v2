package junjange.core.local.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import junjange.core.data.datasource.LocalDataSource
import junjange.core.data.datasource.LotteryRoomDataSource
import junjange.core.data.datasource.NotificationLocalDataSource
import junjange.core.data.datasource.PensionLotteryRoomDataSource
import junjange.core.local.datasource.LocalDataSourceImpl
import junjange.core.local.datasource.LotteryRoomDataSourceImpl
import junjange.core.local.datasource.NotificationLocalDataSourceImpl
import junjange.core.local.datasource.PensionLotteryRoomDataSourceImpl

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

    @Binds
    abstract fun bindsNotificationLocalDataSource(
        notificationLocalDataSourceImpl: NotificationLocalDataSourceImpl,
    ): NotificationLocalDataSource
}
