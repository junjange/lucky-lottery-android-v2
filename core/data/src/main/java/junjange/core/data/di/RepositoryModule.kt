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
import org.koin.dsl.module

val repositoryModule =
    module {
        single<KakaoLoginRepository> { KakaoLoginRepositoryImpl(get()) }
        single<FirebaseRepository> { FirebaseRepositoryImpl(get()) }
        single<LocalRepository> { LocalRepositoryImpl(get()) }
        single<CredentialRepository> { CredentialRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get(), get()) }
        single<LotteryRepository> { LotteryRepositoryImpl(get(), get()) }
        single<PensionLotteryRepository> { PensionLotteryRepositoryImpl(get(), get(), get()) }
        single<WinningRepository> { WinningRepositoryImpl(get()) }
        single<NotificationRepository> { NotificationRepositoryImpl(get()) }
        single<ImagesRepository> { ImagesRepositoryImpl(get()) }
        single<GoogleRepository> { GoogleRepositoryImpl(get()) }
    }
