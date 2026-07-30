package junjange.shared.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import junjange.core.domain.model.LottoType
import junjange.core.ui.resources.Res
import junjange.core.ui.resources.ic_clover
import junjange.core.ui.resources.ic_clover_outlined
import junjange.core.ui.resources.ic_home
import junjange.core.ui.resources.ic_home_outlined
import junjange.core.ui.resources.ic_plus
import junjange.core.ui.resources.ic_settings
import junjange.core.ui.resources.ic_settings_outlined
import junjange.feature.home.HomeScreen
import junjange.feature.home.HomeViewModel
import junjange.feature.mynumber.MyNumberScreen
import junjange.feature.notification.NotificationRoute
import junjange.feature.notification.NotificationViewModel
import junjange.feature.randomnumber.RandomNumberScreen
import junjange.feature.randomnumber.RandomNumberViewModel
import junjange.feature.randomnumbergeneration.RandomNumberGenerationScreen
import junjange.feature.randomnumbergeneration.RandomNumberGenerationViewModel
import junjange.feature.setting.SettingScreen
import junjange.feature.setting.SettingViewModel
import junjange.feature.splash.SplashScreen
import junjange.feature.splash.SplashViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

/**
 * Shared Compose-Multiplatform navigation graph, in commonMain so both platforms can
 * use it. iOS renders this in place of the previous hand-rolled enum navigation.
 * Screens are wired in as they migrate to commonMain.
 */
object Routes {
    const val SPLASH = "splash"
    const val MAIN = "main"
    const val RANDOM_GENERATION = "random_generation/{lottoType}"
    const val NOTIFICATION = "notification"

    fun randomGeneration(lottoType: String): String = "random_generation/$lottoType"
}

@Composable
fun LotteryNavHost(
    onLaunchQrScanner: (() -> Unit)? = null,
) {
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

        composable(Routes.MAIN) { backStackEntry ->
            val requestedMyNumberPage by backStackEntry.savedStateHandle
                .getStateFlow<String?>(KEY_MY_NUMBER_PAGE, null)
                .collectAsState()
            MainTabs(
                requestedMyNumberPage = requestedMyNumberPage,
                onMyNumberPageConsumed = { backStackEntry.savedStateHandle[KEY_MY_NUMBER_PAGE] = null },
                onLaunchQrScanner = onLaunchQrScanner,
                onNavigateToRandomGeneration = { lottoType ->
                    navController.navigate(Routes.randomGeneration(lottoType.name))
                },
                onNavigateToNotification = { _, _ -> navController.navigate(Routes.NOTIFICATION) },
            )
        }

        composable(
            route = Routes.RANDOM_GENERATION,
            arguments = listOf(navArgument("lottoType") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 4 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 4 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        ) { backStackEntry ->
            val lottoType = backStackEntry.arguments?.read { getStringOrNull("lottoType") }.orEmpty()
            RandomNumberGenerationScreen(
                viewModel = koinInject<RandomNumberGenerationViewModel> { parametersOf(lottoType) },
                navigateToMain = { initialPage ->
                    runCatching { navController.getBackStackEntry(Routes.MAIN) }
                        .getOrNull()
                        ?.savedStateHandle
                        ?.set(KEY_MY_NUMBER_PAGE, initialPage)
                    navController.popBackStack(Routes.MAIN, inclusive = false)
                },
                onBack = { navController.popBackStack() },
            )
        }

        // 뒤로 가기(←)로 빠져나가는 화면은 오른쪽에서 들어온다. 아래에서 올라오는 것은
        // 닫기(X)로 빠져나가는 화면(번호 담기)의 방향이라 둘을 구분한다.
        composable(
            route = Routes.NOTIFICATION,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 4 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 4 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
        ) {
            NotificationRoute(
                viewModel = koinInject<NotificationViewModel>(),
                finish = { navController.popBackStack() },
            )
        }
    }
}

private const val KEY_MY_NUMBER_PAGE = "myNumberPage"

private enum class Tab(
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
) {
    HOME("홈", Res.drawable.ic_home, Res.drawable.ic_home_outlined),
    MY_NUMBER("내 번호", Res.drawable.ic_clover, Res.drawable.ic_clover_outlined),
    RANDOM_NUMBER("랜덤 번호", Res.drawable.ic_plus, Res.drawable.ic_plus),
    SETTING("설정", Res.drawable.ic_settings, Res.drawable.ic_settings_outlined),
}

/** 열거형은 KMP 공통 코드에서 자동 저장 대상이 아니라 이름 문자열로 저장한다. */
private val TabSaver: Saver<Tab, String> =
    Saver(
        save = { it.name },
        restore = { name -> Tab.entries.firstOrNull { it.name == name } },
    )

@Composable
private fun MainTabs(
    requestedMyNumberPage: String?,
    onMyNumberPageConsumed: () -> Unit,
    onLaunchQrScanner: (() -> Unit)?,
    onNavigateToRandomGeneration: (LottoType) -> Unit,
    onNavigateToNotification: (lottoNotificationState: Boolean, pensionLottoNotificationState: Boolean) -> Unit,
) {
    var selectedTab by rememberSaveable(stateSaver = TabSaver) { mutableStateOf(Tab.HOME) }
    var myNumberInitialPage by rememberSaveable { mutableStateOf(0) }

    // 탭 바를 내려야 하는 화면이 떠 있는지. 삭제 모드에서는 하단에 삭제 버튼만 남아야 하고,
    // 번호를 담는 화면은 전체 화면이라 탭 바가 남아 있으면 아래가 두 겹이 되고
    // 고르던 중에 다른 탭으로 새어나갈 수 있다.
    var isChromeHidden by remember { mutableStateOf(false) }

    LaunchedEffect(requestedMyNumberPage) {
        requestedMyNumberPage ?: return@LaunchedEffect
        myNumberInitialPage = requestedMyNumberPage.toIntOrNull() ?: 0
        selectedTab = Tab.MY_NUMBER
        onMyNumberPageConsumed()
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AnimatedVisibility(
                visible = !isChromeHidden,
                // 아래로 미끄러져 나가면서 자리도 함께 접는다. 슬라이드만 주면 애니메이션이 끝나는
                // 순간 비어 있던 자리가 한 번에 사라져 위 콘텐츠가 툭 내려앉는다.
                enter = expandVertically() + slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically(),
            ) {
                NavigationBar {
                    Tab.entries.forEach { tab ->
                        val selected = selectedTab == tab
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(if (selected) tab.selectedIcon else tab.unselectedIcon),
                                    contentDescription = tab.label,
                                )
                            },
                            label = { Text(tab.label) },
                            selected = selected,
                            onClick = { selectedTab = tab },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                androidx.compose.ui.Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            when (selectedTab) {
                Tab.HOME -> {
                    val qrScanAndOpen = junjange.feature.home.rememberQrScanAndOpen()
                    HomeScreen(
                        viewModel = koinInject<HomeViewModel>(),
                        navigateToQRScanner = onLaunchQrScanner ?: qrScanAndOpen ?: {},
                    )
                }

                Tab.MY_NUMBER ->
                    MyNumberScreen(
                        viewModel = koinInject(),
                        initialPage = myNumberInitialPage,
                        onChromeHidden = { isChromeHidden = it },
                    )

                Tab.RANDOM_NUMBER ->
                    RandomNumberScreen(
                        viewModel = remember { RandomNumberViewModel() },
                        navigateRandomNumberGeneration = onNavigateToRandomGeneration,
                    )

                Tab.SETTING -> {
                    val settingActions = junjange.feature.setting.rememberSettingActions()
                    SettingScreen(
                        viewModel = koinInject<SettingViewModel>(),
                        navigateToNotification = onNavigateToNotification,
                        onOpenUrl = settingActions.openUrl,
                        onOpenReview = settingActions.openReview,
                        versionName = settingActions.versionName,
                    )
                }
            }
        }
    }
}
