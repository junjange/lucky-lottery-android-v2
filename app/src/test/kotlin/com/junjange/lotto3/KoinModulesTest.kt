package com.junjange.lotto3

import com.junjange.lotto3.di.appModule
import com.junjange.lotto3.di.navigatorModule
import junjange.core.data.di.repositoryModule
import junjange.core.domain.di.useCaseModule
import junjange.core.firebase.di.firebaseDataSourceModule
import junjange.core.firebase.di.firebaseModule
import junjange.core.firebase.di.googleModule
import junjange.core.kakao.di.kakaoDataSourceModule
import junjange.core.local.di.localDataSourceModule
import junjange.core.local.di.localPlatformModule
import junjange.core.local.di.providerModule
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
import org.junit.After
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

/**
 * Koin 모듈 검증 테스트
 *
 * 이 테스트는 컴파일 타임에 가까운 시점에 모든 의존성을 검증합니다:
 * - 모든 의존성이 올바르게 주입되는지 확인
 * - 순환 의존성 체크
 * - 타입 불일치 발견
 * - 누락된 모듈 발견
 */
class KoinModulesTest : KoinTest {

    @After
    fun tearDown() {
        org.koin.core.context.stopKoin()
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `verify all koin modules - compile time dependency check`() {
        // App modules
        appModule.verify()
        navigatorModule.verify()

        // Core modules
        localPlatformModule.verify()
        localDataSourceModule.verify()
        remoteModule.verify()
        remoteDataSourceModule.verify()
        repositoryModule.verify()
        useCaseModule.verify()

        // Firebase modules
        firebaseModule.verify()
        googleModule.verify()
        firebaseDataSourceModule.verify()

        // Other core modules
        kakaoDataSourceModule.verify()
        ocrModule.verify()
        providerModule.verify()

        // Feature ViewModels
        homeViewModelModule.verify()
        loginViewModelModule.verify()
        mainViewModelModule.verify()
        myViewModelModule.verify()
        myNumberViewModelModule.verify()
        notificationViewModelModule.verify()
        randomNumberViewModelModule.verify()
        randomNumberGenerationViewModelModule.verify()
        registerViewModelModule.verify()
        settingViewModelModule.verify()
        splashViewModelModule.verify()
        withdrawalViewModelModule.verify()
        editProfileViewModelModule.verify()
    }
}
