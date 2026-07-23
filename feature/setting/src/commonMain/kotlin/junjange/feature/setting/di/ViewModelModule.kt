package junjange.feature.setting.di

import junjange.feature.setting.SettingViewModel
import org.koin.dsl.module
val settingViewModelModule = module {
    factory {
        SettingViewModel()
    }
}
