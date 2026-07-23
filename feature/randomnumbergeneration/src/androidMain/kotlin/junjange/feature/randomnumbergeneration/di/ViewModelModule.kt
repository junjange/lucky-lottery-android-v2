package junjange.feature.randomnumbergeneration.di

import junjange.feature.randomnumbergeneration.RandomNumberGenerationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val randomNumberGenerationViewModelModule = module {
    viewModel {
        RandomNumberGenerationViewModel(
            savedStateHandle = get(),
            getLotteryRandomUseCase = get(),
            insertLotteryUseCase = get(),
            getPensionLotteryRandomUseCase = get(),
            insertPensionLotteryUseCase = get()
        )
    }
}
