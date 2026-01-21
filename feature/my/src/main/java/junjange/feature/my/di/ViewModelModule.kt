package junjange.feature.my.di

import junjange.feature.my.MyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myViewModelModule = module {
    viewModel {
        MyViewModel(
            getUserMyInfoUseCase = get(),
            postLogoutUseCase = get(),
            deleteLocalDataUseCase = get()
        )
    }
}
