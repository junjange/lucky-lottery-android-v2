package junjange.shared.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import junjange.feature.home.HomeScreen
import junjange.feature.home.HomeViewModel
import junjange.feature.notification.NotificationScreen
import junjange.feature.notification.NotificationViewModel
import junjange.feature.randomnumber.RandomNumberScreen
import junjange.feature.randomnumber.RandomNumberViewModel
import junjange.feature.randomnumbergeneration.RandomNumberGenerationScreen
import junjange.feature.randomnumbergeneration.RandomNumberGenerationViewModel
import junjange.feature.setting.SettingScreen
import junjange.feature.setting.SettingViewModel
import junjange.feature.splash.SplashScreen
import junjange.feature.splash.SplashViewModel
import org.koin.compose.koinInject

/**
 * Shared Compose-Multiplatform navigation graph, in commonMain so both platforms can
 * use it. iOS renders this in place of the previous hand-rolled enum navigation.
 * Screens are wired in as they migrate to commonMain.
 */
object Routes {
    const val SPLASH = "splash"
    const val MAIN = "main"
    const val RANDOM_GENERATION = "random_generation"
    const val NOTIFICATION = "notification"
}

@Composable
fun LotteryNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                viewModel = koinInject<SplashViewModel>(),
                navigateToMain = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.MAIN) {
            MainTabs(
                onNavigateToRandomGeneration = { navController.navigate(Routes.RANDOM_GENERATION) },
                onNavigateToNotification = { _, _ -> navController.navigate(Routes.NOTIFICATION) },
            )
        }

        composable(Routes.RANDOM_GENERATION) {
            RandomNumberGenerationScreen(
                viewModel = koinInject<RandomNumberGenerationViewModel>(),
                navigateToMain = { navController.popBackStack(Routes.MAIN, inclusive = false) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.NOTIFICATION) {
            NotificationScreen(
                viewModel = koinInject<NotificationViewModel>(),
                finish = { navController.popBackStack() },
                onRequestLottoNotification = {},
                onRequestPensionLottoNotification = {},
                showPermissionSettingsDialog = false,
                onDismissPermissionDialog = {},
                onNavigateToSettings = {},
            )
        }
    }
}

private enum class Tab { HOME, RANDOM, SETTING }

@Composable
private fun MainTabs(
    onNavigateToRandomGeneration: () -> Unit,
    onNavigateToNotification: (lottoNotificationState: Boolean, pensionLottoNotificationState: Boolean) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(Tab.HOME) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == Tab.HOME,
                    onClick = { selectedTab = Tab.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("홈") },
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.RANDOM,
                    onClick = { selectedTab = Tab.RANDOM },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("랜덤 번호") },
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.SETTING,
                    onClick = { selectedTab = Tab.SETTING },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("설정") },
                )
            }
        },
    ) { innerPadding ->
        when (selectedTab) {
            Tab.HOME ->
                HomeScreen(
                    viewModel = koinInject<HomeViewModel>(),
                    navigateToQRScanner = {},
                )

            Tab.RANDOM ->
                RandomNumberScreen(
                    viewModel = remember { RandomNumberViewModel() },
                    navigateRandomNumberGeneration = { onNavigateToRandomGeneration() },
                    onBack = { selectedTab = Tab.HOME },
                )

            Tab.SETTING ->
                SettingScreen(
                    viewModel = koinInject<SettingViewModel>(),
                    navigateToNotification = onNavigateToNotification,
                )
        }
        // innerPadding intentionally consumed by each screen's own scaffolding.
        innerPadding
    }
}
