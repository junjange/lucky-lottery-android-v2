package junjange.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.navigation.Destination

@Composable
fun LottoBottomBar(
    currentDestination: NavDestination?,
    onNavigate: (Destination) -> Unit,
    navigateToActivity: () -> Unit,
) {
    Column {
        NavigationBar(containerColor = LottoTheme.colors.white) {
            Destination.entries.forEach { destination ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == destination.route } == true

                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = if (selected) destination.selectedIconRes else destination.inSelectedIconRes),
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = destination.labelResId),
                            style =
                                MaterialTheme.typography.labelSmall.copy(
                                    color = if (selected) LottoTheme.colors.black else LottoTheme.colors.gray400,
                                ),
                        )
                    },
                    selected = false,
                    onClick = {
                        if (destination == Destination.RANDOM_NUMBER) {
                            navigateToActivity()
                        } else {
                            onNavigate(destination)
                        }
                    },
                    interactionSource = MutableInteractionSource(),
                )
            }
        }
    }
}
