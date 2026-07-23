package com.junjange.lotto3

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
import org.junit.After
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verify

/**
 * Koin 모듈 검증 테스트
 *
 * 모든 모듈을 하나의 umbrella 모듈로 묶어 verify()로 검증합니다:
 * - 모든 의존성이 올바르게 주입되는지 확인
 * - 순환 의존성 체크
 * - 타입 불일치/누락된 정의 발견
 *
 * extraTypes: 런타임에 플랫폼/프레임워크가 제공하는 타입들(androidContext 등)과
 * verify()가 서드파티 생성자에서 오탐하는 타입들.
 */
class KoinModulesTest : KoinTest {

    @After
    fun tearDown() {
        org.koin.core.context.stopKoin()
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `verify all koin modules - compile time dependency check`() {
        module {
            includes(
                // App modules
                appModule,
                // Core modules
                localPlatformModule,
                localDataSourceModule,
                remoteModule,
                remoteDataSourceModule,
                repositoryModule,
                useCaseModule,
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
        }.verify(
            extraTypes =
                listOf(
                    android.content.Context::class,
                    Class::class,
                    Int::class,
                    Boolean::class,
                    io.ktor.client.engine.HttpClientEngine::class,
                    io.ktor.client.HttpClientConfig::class,
                    com.googlecode.tesseract.android.TessBaseAPI.ProgressNotifier::class,
                    androidx.lifecycle.SavedStateHandle::class,
                ),
        )
    }
}
