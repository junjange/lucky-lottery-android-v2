package junjange.feature.mynumber.di

import junjange.feature.mynumber.MyNumberViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myNumberViewModelModule = module {
    viewModel {
        MyNumberViewModel(
            ocrService = get(),
            insertLotteryUseCase = get(),
            loadLotteryRoundsUseCase = get(),
            insertPensionLotteryUseCase = get(),
            loadPensionLotteryRoundsUseCase = get(),
            deleteLotteryByRoundAndIdUseCase = get(),
            deletePensionLotteryByRoundAndIdUseCase = get(),
            deleteAllLotteryUseCase = get(),
            deleteAllPensionLotteryUseCase = get(),
        )
    }
}
