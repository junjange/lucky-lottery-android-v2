package junjange.feature.mynumber.di

import junjange.feature.mynumber.MyNumberViewModel
import org.koin.dsl.module

val myNumberViewModelModule = module {
    factory {
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
