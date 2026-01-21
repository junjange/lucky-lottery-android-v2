package junjange.feature.withdrawal.di

import junjange.feature.withdrawal.WithdrawViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val withdrawalViewModelModule = module {
    viewModel {
        WithdrawViewModel(
            savedStateHandle = get(),
            googleOauthTokenUseCase = get(),
            deleteMeUseCase = get(),
            deleteLocalDataUseCase = get()
        )
    }
}
