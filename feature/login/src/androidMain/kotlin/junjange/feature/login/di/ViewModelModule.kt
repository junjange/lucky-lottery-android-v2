package junjange.feature.login.di

import junjange.feature.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel {
        LoginViewModel(
            kakaoLoginUseCase = get(),
            getValidRegisterUseCase = get(),
            postLoginUseCase = get(),
            saveJwtTokenUseCase = get(),
            postNotificationRegisterTokenUseCase = get(),
            getFCMTokenUseCase = get()
        )
    }
}
