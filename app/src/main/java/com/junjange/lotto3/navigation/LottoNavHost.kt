package com.junjange.lotto3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import junjange.feature.main.MainScreen
import junjange.feature.notification.NotificationRoute
import junjange.feature.randomnumber.RandomNumberScreen
import junjange.feature.randomnumbergeneration.RandomNumberGenerationScreen
import junjange.feature.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

object LottoRoutes {
    const val SPLASH = "splash"
    const val MAIN = "main?initialPage={initialPage}"
    const val RANDOM_NUMBER = "random_number"
    const val RANDOM_GENERATION = "random_generation/{lottoType}"
    const val NOTIFICATION = "notification"

    fun main(initialPage: String? = null): String = if (initialPage != null) "main?initialPage=$initialPage" else "main"

    fun randomGeneration(lottoType: String): String = "random_generation/$lottoType"
}

@Composable
fun LottoNavHost(
    navigateToQRScanner: () -> Unit,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LottoRoutes.SPLASH) {
        composable(LottoRoutes.SPLASH) {
            SplashScreen(
                viewModel = koinViewModel(),
                navigateToMain = {
                    navController.navigate(LottoRoutes.main()) {
                        popUpTo(LottoRoutes.SPLASH) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = LottoRoutes.MAIN,
            arguments =
                listOf(
                    navArgument("initialPage") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                ),
        ) { backStackEntry ->
            MainScreen(
                viewModel = koinViewModel(),
                initialPage = backStackEntry.arguments?.getString("initialPage"),
                navigateToQRScanner = navigateToQRScanner,
                navigateToRandomNumber = {
                    navController.navigate(LottoRoutes.RANDOM_NUMBER) {
                        launchSingleTop = true
                    }
                },
                navigateToNotification = { _, _ ->
                    navController.navigate(LottoRoutes.NOTIFICATION) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(LottoRoutes.RANDOM_NUMBER) {
            RandomNumberScreen(
                viewModel = koinViewModel(),
                navigateRandomNumberGeneration = { lottoType ->
                    navController.navigate(LottoRoutes.randomGeneration(lottoType.name))
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = LottoRoutes.RANDOM_GENERATION,
            arguments = listOf(navArgument("lottoType") { type = NavType.StringType }),
        ) {
            RandomNumberGenerationScreen(
                viewModel = koinViewModel(),
                navigateToMain = { initialPage ->
                    navController.navigate(LottoRoutes.main(initialPage)) {
                        popUpTo(LottoRoutes.MAIN) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(LottoRoutes.NOTIFICATION) {
            NotificationRoute(
                viewModel = koinViewModel(),
                finish = { navController.popBackStack() },
            )
        }
    }
}
