package junjange.feature.randomnumber.di

import junjange.feature.randomnumber.RandomNumberViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val randomNumberViewModelModule = module {
    viewModel {
        RandomNumberViewModel()
    }
}
