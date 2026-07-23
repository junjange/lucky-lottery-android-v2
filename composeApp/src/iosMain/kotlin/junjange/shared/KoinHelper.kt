package junjange.shared

import androidx.lifecycle.SavedStateHandle
import junjange.core.local.di.localModules
import junjange.core.ocr.di.ocrModule
import junjange.feature.mynumber.di.myNumberViewModelModule
import junjange.feature.home.HomeViewModel
import junjange.feature.notification.NotificationViewModel
import junjange.feature.randomnumber.RandomNumberViewModel
import junjange.feature.randomnumbergeneration.RandomNumberGenerationViewModel
import junjange.feature.setting.SettingViewModel
import junjange.feature.splash.SplashViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val iosViewModelModule = module {
    factory { SplashViewModel() }
    factory { HomeViewModel(get(), get(), get(), get()) }
    factory { SettingViewModel() }
    factory { RandomNumberViewModel() }
    factory { NotificationViewModel(get(), get(), get()) }
    factory { RandomNumberGenerationViewModel(SavedStateHandle(), get(), get(), get(), get()) }
}

fun startKoinApp() {
    startKoin {
        modules(
            buildList {
                addAll(localModules)
                add(ocrModule)
                add(myNumberViewModelModule)
                addAll(sharedModules)
                add(iosViewModelModule)
            },
        )
    }
}
