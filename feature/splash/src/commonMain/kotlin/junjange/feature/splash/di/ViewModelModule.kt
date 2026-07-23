package junjange.feature.splash.di

import junjange.feature.splash.SplashViewModel
import org.koin.dsl.module
val splashViewModelModule = module {
    factory {
        SplashViewModel(
            getJwtTokenUseCase = get()
        )
    }
}
