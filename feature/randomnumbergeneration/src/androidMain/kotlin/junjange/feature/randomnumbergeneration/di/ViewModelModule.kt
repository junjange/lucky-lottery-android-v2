package junjange.feature.randomnumbergeneration.di

import androidx.lifecycle.SavedStateHandle
import junjange.feature.randomnumbergeneration.RandomNumberGenerationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val randomNumberGenerationViewModelModule = module {
    // LotteryNavHost가 koinInject { parametersOf(lottoType) }로 생성하므로
    // SavedStateHandle은 파라미터로 직접 구성한다 (iOS KoinHelper와 동일 패턴)
    viewModel { params ->
        RandomNumberGenerationViewModel(
            savedStateHandle = SavedStateHandle(mapOf("lottoType" to params.getOrNull<String>())),
            getLotteryRandomUseCase = get(),
            insertLotteryUseCase = get(),
            getPensionLotteryRandomUseCase = get(),
            insertPensionLotteryUseCase = get()
        )
    }
}
