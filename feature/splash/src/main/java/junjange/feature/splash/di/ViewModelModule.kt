package junjange.feature.splash.di

import junjange.feature.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashViewModelModule = module {
    viewModel {
        SplashViewModel(
            getJwtTokenUseCase = get()
        )
    }
}
