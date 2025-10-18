package junjange.core.data.di

import junjange.core.data.repository.CredentialRepositoryImpl
import junjange.core.data.repository.FirebaseRepositoryImpl
import junjange.core.data.repository.GoogleRepositoryImpl
import junjange.core.data.repository.ImagesRepositoryImpl
import junjange.core.data.repository.KakaoLoginRepositoryImpl
import junjange.core.data.repository.LocalRepositoryImpl
import junjange.core.data.repository.LotteryRepositoryImpl
import junjange.core.data.repository.NotificationRepositoryImpl
import junjange.core.data.repository.PensionLotteryRepositoryImpl
import junjange.core.data.repository.UserRepositoryImpl
import junjange.core.data.repository.WinningRepositoryImpl
import junjange.core.domain.repository.CredentialRepository
import junjange.core.domain.repository.FirebaseRepository
import junjange.core.domain.repository.GoogleRepository
import junjange.core.domain.repository.ImagesRepository
import junjange.core.domain.repository.KakaoLoginRepository
import junjange.core.domain.repository.LocalRepository
import junjange.core.domain.repository.LotteryRepository
import junjange.core.domain.repository.NotificationRepository
import junjange.core.domain.repository.PensionLotteryRepository
import junjange.core.domain.repository.UserRepository
import junjange.core.domain.repository.WinningRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindKakaoLoginRepository(kakaoLoginRepositoryImpl: KakaoLoginRepositoryImpl): KakaoLoginRepository

    @Binds
    @Singleton
    abstract fun bindFirebaseRepository(firebaseRepositoryImpl: FirebaseRepositoryImpl): FirebaseRepository

    @Binds
    @Singleton
    abstract fun bindLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    @Singleton
    abstract fun bindCredentialRepository(credentialRepositoryImpl: CredentialRepositoryImpl): CredentialRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindLotteryRepository(lotteryRepositoryImpl: LotteryRepositoryImpl): LotteryRepository

    @Binds
    @Singleton
    abstract fun bindPensionLotteryRepository(pensionLotteryRepositoryImpl: PensionLotteryRepositoryImpl): PensionLotteryRepository

    @Binds
    @Singleton
    abstract fun bindWinningRepository(winningRepositoryImpl: WinningRepositoryImpl): WinningRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindImagesRepository(imagesRepositoryImpl: ImagesRepositoryImpl): ImagesRepository

    @Binds
    @Singleton
    abstract fun bindGoogleRepository(googleRepositoryImpl: GoogleRepositoryImpl): GoogleRepository
}
