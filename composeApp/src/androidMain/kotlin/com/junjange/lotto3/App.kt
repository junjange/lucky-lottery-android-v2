package com.junjange.lotto3

import android.app.Application
import androidx.work.Configuration
import com.google.android.gms.ads.MobileAds
import com.junjange.lotto3.di.appModule
import junjange.core.data.di.repositoryModule
import junjange.core.domain.di.useCaseModule
import junjange.core.local.di.localDataSourceModule
import junjange.core.local.di.localPlatformModule
import junjange.core.ocr.di.ocrModule
import junjange.core.remote.di.remoteDataSourceModule
import junjange.core.remote.di.remoteModule
import junjange.feature.home.di.homeViewModelModule
import junjange.feature.mynumber.di.myNumberViewModelModule
import junjange.feature.notification.di.notificationViewModelModule
import junjange.feature.randomnumber.di.randomNumberViewModelModule
import junjange.feature.randomnumbergeneration.di.randomNumberGenerationViewModelModule
import junjange.feature.setting.di.settingViewModelModule
import junjange.feature.splash.di.splashViewModelModule
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

                // Core modules
                localPlatformModule,
                localDataSourceModule,
                remoteModule,
                remoteDataSourceModule,
                repositoryModule,
                useCaseModule,

                // Other core modules
                ocrModule,

                // Feature ViewModels
                homeViewModelModule,
                myNumberViewModelModule,
                notificationViewModelModule,
                randomNumberViewModelModule,
                randomNumberGenerationViewModelModule,
                settingViewModelModule,
                splashViewModelModule,
            )
        }

        MobileAds.initialize(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().build()
}
