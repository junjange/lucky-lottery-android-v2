package com.junjange.lotto3

import android.app.Application
import androidx.work.Configuration
import com.google.android.gms.ads.MobileAds
import com.junjange.lotto3.di.appModule
import com.junjange.lotto3.di.navigatorModule
import com.kakao.sdk.common.KakaoSdk
import junjange.core.data.di.repositoryModule
import junjange.core.domain.di.useCaseModule
import junjange.core.firebase.di.firebaseDataSourceModule
import junjange.core.firebase.di.firebaseModule
import junjange.core.firebase.di.googleModule
import junjange.core.kakao.di.kakaoDataSourceModule
import junjange.core.local.di.localDataSourceModule
import junjange.core.local.di.localModule
import junjange.core.local.di.workManagerModule
import junjange.core.local.provider.providerModule
import junjange.core.notification.di.notificationModule
import junjange.core.ocr.di.ocrModule
import junjange.core.remote.di.remoteDataSourceModule
import junjange.core.remote.di.remoteModule
import junjange.feature.editprofile.di.editProfileViewModelModule
import junjange.feature.home.di.homeViewModelModule
import junjange.feature.login.di.loginViewModelModule
import junjange.feature.main.di.mainViewModelModule
import junjange.feature.my.di.myViewModelModule
import junjange.feature.mynumber.di.myNumberViewModelModule
import junjange.feature.notification.di.notificationViewModelModule
import junjange.feature.randomnumber.di.randomNumberViewModelModule
import junjange.feature.randomnumbergeneration.di.randomNumberGenerationViewModelModule
import junjange.feature.register.di.registerViewModelModule
import junjange.feature.setting.di.settingViewModelModule
import junjange.feature.splash.di.splashViewModelModule
import junjange.feature.withdrawal.di.withdrawalViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App :
    Application(),
    Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            workManagerFactory()
            modules(
                // App modules
                appModule,
                navigatorModule,

                // Core modules
                localModule,
                localDataSourceModule,
                remoteModule,
                remoteDataSourceModule,
                repositoryModule,
                useCaseModule,

                // Firebase modules
                firebaseModule,
                googleModule,
                firebaseDataSourceModule,

                // Other core modules
                kakaoDataSourceModule,
                notificationModule,
                ocrModule,
                workManagerModule,
                providerModule,

                // Feature ViewModels
                homeViewModelModule,
                loginViewModelModule,
                mainViewModelModule,
                myViewModelModule,
                myNumberViewModelModule,
                notificationViewModelModule,
                randomNumberViewModelModule,
                randomNumberGenerationViewModelModule,
                registerViewModelModule,
                settingViewModelModule,
                splashViewModelModule,
                withdrawalViewModelModule,
                editProfileViewModelModule,
            )
        }

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        MobileAds.initialize(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().build()
}
