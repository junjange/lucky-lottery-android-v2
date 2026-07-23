package junjange.feature.my.di

import junjange.feature.my.MyViewModel
import org.koin.dsl.module
val myViewModelModule = module {
    factory {
        MyViewModel(
            getUserMyInfoUseCase = get(),
            postLogoutUseCase = get(),
            deleteLocalDataUseCase = get()
        )
    }
}
