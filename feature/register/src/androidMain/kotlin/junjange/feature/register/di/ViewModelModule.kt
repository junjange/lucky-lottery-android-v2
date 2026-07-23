package junjange.feature.register.di

import junjange.feature.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registerViewModelModule = module {
    viewModel {
        RegisterViewModel(
            savedStateHandle = get(),
            postRegisterUseCase = get(),
            saveJwtTokenUseCase = get(),
            postNotificationRegisterTokenUseCase = get(),
            getFCMTokenUseCase = get()
        )
    }
}
