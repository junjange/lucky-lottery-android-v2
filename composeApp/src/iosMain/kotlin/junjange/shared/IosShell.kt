package junjange.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.domain.model.LottoType
import junjange.core.ui.image.configureImageLoader
import junjange.feature.home.HomeScreen
import junjange.feature.home.HomeViewModel
import junjange.feature.home.rememberQrScanAndOpen
import junjange.feature.mynumber.MyNumberScreen
import junjange.feature.notification.NotificationRoute
import junjange.feature.notification.NotificationViewModel
import junjange.feature.randomnumber.RandomNumberScreen
import junjange.feature.randomnumber.RandomNumberViewModel
import junjange.feature.randomnumbergeneration.RandomNumberGenerationScreen
import junjange.feature.randomnumbergeneration.RandomNumberGenerationViewModel
import junjange.feature.setting.SettingScreen
import junjange.feature.setting.SettingViewModel
import junjange.feature.setting.rememberSettingActions
import junjange.feature.splash.SplashScreen
import junjange.feature.splash.SplashViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import platform.UIKit.UIViewController

/**
 * iOS SwiftUI 셸(TabView/NavigationStack, Liquid Glass)이 embed하는 화면 단위
 * ComposeUIViewController 팩토리. 탭/내비게이션 셸은 Swift가 담당하고,
 * 콘텐츠는 commonMain의 Compose 화면을 그대로 사용한다.
 */
private fun themed(content: @Composable () -> Unit): UIViewController =
    ComposeUIViewController {
        remember {
            configureImageLoader()
            Unit
        }
        LottoTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                // SwiftUI 탭바/홈 인디케이터와 겹치지 않도록 하단 safe area만 패딩
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
                ) {
                    content()
                }
            }
        }
    }

fun splashViewController(onFinished: () -> Unit): UIViewController =
    themed {
        SplashScreen(
            viewModel = koinInject<SplashViewModel>(),
            navigateToMain = onFinished,
        )
    }

fun homeViewController(): UIViewController =
    themed {
        val qrScanAndOpen = rememberQrScanAndOpen()
        HomeScreen(
            viewModel = koinInject<HomeViewModel>(),
            navigateToQRScanner = qrScanAndOpen ?: {},
        )
    }

fun myNumberViewController(initialPage: Int): UIViewController =
    themed {
        MyNumberScreen(
            viewModel = koinInject(),
            initialPage = initialPage,
        )
    }

fun randomNumberViewController(navigateToGeneration: (String) -> Unit): UIViewController =
    themed {
        RandomNumberScreen(
            viewModel = remember { RandomNumberViewModel() },
            navigateRandomNumberGeneration = { lottoType: LottoType -> navigateToGeneration(lottoType.name) },
        )
    }

fun randomNumberGenerationViewController(
    lottoType: String,
    navigateToMyNumber: (String) -> Unit,
    onBack: () -> Unit,
): UIViewController =
    themed {
        RandomNumberGenerationScreen(
            viewModel = koinInject<RandomNumberGenerationViewModel> { parametersOf(lottoType) },
            navigateToMain = navigateToMyNumber,
            onBack = onBack,
        )
    }

fun settingViewController(navigateToNotification: () -> Unit): UIViewController =
    themed {
        val settingActions = rememberSettingActions()
        SettingScreen(
            viewModel = koinInject<SettingViewModel>(),
            navigateToNotification = { _, _ -> navigateToNotification() },
            onOpenUrl = settingActions.openUrl,
            onOpenReview = settingActions.openReview,
            versionName = settingActions.versionName,
        )
    }

fun notificationViewController(onBack: () -> Unit): UIViewController =
    themed {
        NotificationRoute(
            viewModel = koinInject<NotificationViewModel>(),
            finish = onBack,
        )
    }
