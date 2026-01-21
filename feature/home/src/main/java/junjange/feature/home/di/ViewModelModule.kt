package junjange.feature.home.di

import junjange.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeViewModelModule = module {
    viewModel {
        HomeViewModel(
            getLotteryRoundUseCase = get(),
            getPensionLotteryRoundUseCase = get(),
            getLotteryUseCase = get(),
            getPensionLotteryUseCase = get()
        )
    }
}
