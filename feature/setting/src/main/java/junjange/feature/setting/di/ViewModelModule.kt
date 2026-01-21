package junjange.feature.setting.di

import junjange.feature.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingViewModelModule = module {
    viewModel {
        SettingViewModel()
    }
}
