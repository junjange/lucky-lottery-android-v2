package junjange.shared

import androidx.lifecycle.SavedStateHandle
import junjange.core.firebase.di.firebaseDataSourceModule
import junjange.core.firebase.di.firebaseModule
import junjange.core.firebase.di.googleModule
import junjange.core.kakao.di.kakaoDataSourceModule
import junjange.core.local.di.localModules
import junjange.core.ocr.di.ocrModule
import junjange.core.remote.api.AuthenticationListener
import junjange.core.remote.api.BaseUrl
import junjange.feature.home.HomeViewModel
import junjange.feature.login.LoginViewModel
import junjange.feature.notification.NotificationViewModel
import junjange.feature.randomnumber.RandomNumberViewModel
import junjange.feature.randomnumbergeneration.RandomNumberGenerationViewModel
import junjange.feature.setting.SettingViewModel
import junjange.feature.splash.SplashViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * iOS-provided runtime configuration. Persistence (Room), key-value storage
 * (NSUserDefaults), OCR (Vision), and the auth data sources now come from the real
 * platform modules below — no mocks. Kakao/FCM/Google sign-in delegate to optional
 * Swift bridges (KakaoLoginBridge / FcmTokenBridge); register them from Swift to
 * enable those flows.
 */
private val iosConfigModule = module {
    single { BaseUrl("https://www.dhlottery.co.kr") }
    single<AuthenticationListener> {
        object : AuthenticationListener {
            override fun onSessionExpired() {}
        }
    }
}

private val iosViewModelModule = module {
    factory { SplashViewModel(get()) }
    factory { LoginViewModel(get(), get(), get(), get(), get(), get()) }
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
                add(iosConfigModule)
                addAll(localModules)
                add(ocrModule)
                add(kakaoDataSourceModule)
                add(googleModule)
                add(firebaseDataSourceModule)
                add(firebaseModule)
                addAll(sharedModules)
                add(iosViewModelModule)
            },
        )
    }
}
