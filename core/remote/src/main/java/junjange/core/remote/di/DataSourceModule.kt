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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    abstract fun bindsCredentialDataSource(credentialDataSourceImpl: CredentialDataSourceImpl): CredentialDataSource

    @Binds
    abstract fun bindsUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    abstract fun bindsLotteryDataSource(lotteryDataSourceImpl: LotteryDataSourceImpl): LotteryDataSource

    @Binds
    abstract fun bindsPensionLotteryDataSource(pensionLotteryDataSourceImpl: PensionLotteryDataSourceImpl): PensionLotteryDataSource

    @Binds
    abstract fun bindsWinningDataSource(winningDataSourceImpl: WinningDataSourceImpl): WinningDataSource

    @Binds
    abstract fun bindsNotificationDataSource(notificationDataSourceImpl: NotificationDataSourceImpl): NotificationDataSource

    @Binds
    abstract fun bindsImagesDataSource(imagesDataSourceImpl: ImagesDataSourceImpl): ImagesDataSource
}
