package junjange.core.data.di

import junjange.core.data.repository.LotteryRepositoryImpl
import junjange.core.data.repository.PensionLotteryRepositoryImpl
import junjange.core.data.repository.UserRepositoryImpl
import junjange.core.domain.repository.LotteryRepository
import junjange.core.domain.repository.PensionLotteryRepository
import junjange.core.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<LotteryRepository> { LotteryRepositoryImpl(get(), get()) }
        single<PensionLotteryRepository> { PensionLotteryRepositoryImpl(get(), get()) }
    }
