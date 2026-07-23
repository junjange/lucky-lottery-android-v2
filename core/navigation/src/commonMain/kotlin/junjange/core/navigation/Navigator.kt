package junjange.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class Navigator(
    private val navController: NavController,
) {
    fun navigateTo(destination: String) {
        navController.navigate(destination) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateTo(
        route: String,
        args: Map<String, Any>,
    ) {
        var routeWithArgs = route
        args.forEach { (key, value) ->
            routeWithArgs = routeWithArgs.replace("{$key}", value.toString())
        }

        navController.navigate(routeWithArgs) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
fun rememberNavigator(navController: NavController) = remember(navController) { Navigator(navController) }
