@file:JvmName("PlatformComponentsAndroid")

package junjange.core.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import junjange.core.ui.component.AdmobBanner

@Composable
actual fun PlatformAdBanner(modifier: Modifier) {
    AdmobBanner(modifier = modifier)
}
