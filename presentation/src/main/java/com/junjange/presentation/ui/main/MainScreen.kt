package com.junjange.presentation.ui.main

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.junjange.presentation.component.LottoBottomBar
import com.junjange.presentation.ui.home.HomeScreen
import com.junjange.presentation.ui.my.OauthProvider
import com.junjange.presentation.ui.mynumber.MyNumberScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navigateToQRScanner: () -> Unit,
    navigateToRandomNumber: () -> Unit,
    navigateToEditProfile: (nickname: String, profilePath: String?) -> Unit,
    navigateToWithdrawal: (oauthProvider: OauthProvider) -> Unit,
    navigateToSplash: () -> Unit,
    navigateToNotification: (lottoNotificationState: Boolean, pensionLottoNotificationState: Boolean) -> Unit,
) {
    val navController = rememberNavController()
    val navigator = rememberNavigator(navController = navController)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(navController) {
        val initialPage =
            activity?.intent?.getStringExtra(MainActivity.PUT_EXTRA_INITIAL_PAGE)
                ?: return@LaunchedEffect
        navigator.navigateTo(Destination.MY_NUMBER, mapOf(MainActivity.INITIAL_PAGE to initialPage))
    }

    Scaffold(
        bottomBar = {
            LottoBottomBar(
                currentDestination = currentDestination,
                onNavigate = { navigator.navigateTo(it) },
                navigateToActivity = navigateToRandomNumber,
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.HOME.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Destination.HOME.route) { HomeScreen(navigateToQRScanner = navigateToQRScanner) }
            composable(Destination.MY_NUMBER.route) { backStackEntry ->
                val bundle = backStackEntry.arguments
                val initialPage = bundle?.getString(MainActivity.INITIAL_PAGE)?.toIntOrNull() ?: 0
                MyNumberScreen(initialPage = initialPage)
            }
            // TODO 서버 로직 제거로 인해 내정보 탭바 임시 제거
//            composable(Destination.MY.route) {
//                MyScreen(
//                    navigateToWithdrawal = navigateToWithdrawal,
//                    navigateToSplash = navigateToSplash,
//                    navigateToEditProfile = navigateToEditProfile,
//                    navigateToNotification = navigateToNotification,
//                )
//            }
        }
    }
}
