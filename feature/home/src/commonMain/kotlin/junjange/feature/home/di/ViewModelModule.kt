package junjange.feature.home.di

import junjange.feature.home.HomeViewModel
import org.koin.dsl.module
val homeViewModelModule = module {
    factory {
        HomeViewModel(
            getLotteryRoundUseCase = get(),
            getPensionLotteryRoundUseCase = get(),
            getLotteryUseCase = get(),
            getPensionLotteryUseCase = get()
        )
    }
}
