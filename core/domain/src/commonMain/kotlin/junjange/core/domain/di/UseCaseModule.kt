package junjange.core.domain.di

import junjange.core.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    // Lottery UseCases
    factory { GetLotteryUseCase(get()) }
    factory { GetLotteryRoundUseCase(get()) }
    factory { GetLotteryRandomUseCase(get()) }
    factory { InsertLotteryUseCase(get()) }
    factory { LoadLotteryRoundsUseCase(get()) }
    factory { DeleteLotteryByRoundAndIdUseCase(get()) }
    factory { DeleteAllLotteryUseCase(get()) }

    // Pension Lottery UseCases
    factory { GetPensionLotteryUseCase(get()) }
    factory { GetPensionLotteryRoundUseCase(get()) }
    factory { GetPensionLotteryRandomUseCase(get()) }
    factory { InsertPensionLotteryUseCase(get()) }
    factory { LoadPensionLotteryRoundsUseCase(get()) }
    factory { DeletePensionLotteryByRoundAndIdUseCase(get()) }
    factory { DeleteAllPensionLotteryUseCase(get()) }

    // Notification UseCases
    factory { GetNotificationUseCase(get()) }
    factory { PatchLotteryNotificationUseCase(get()) }
    factory { PatchPensionLotteryNotificationUseCase(get()) }
}
