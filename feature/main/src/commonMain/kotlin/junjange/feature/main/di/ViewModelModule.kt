package junjange.feature.main.di

import junjange.feature.main.MainViewModel
import org.koin.dsl.module
val mainViewModelModule = module {
    factory { MainViewModel() }
}
