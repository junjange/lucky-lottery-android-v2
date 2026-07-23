package junjange.core.ui.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView

@Composable
actual fun PlatformAdBanner(modifier: Modifier) {
    val factory = IosAdBridge.bannerFactory
    if (factory != null) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            UIKitView(
                factory = { factory() },
                modifier =
                    Modifier
                        .width(320.dp)
                        .height(50.dp),
            )
        }
    } else {
        Box(modifier = modifier)
    }
}

@Composable
actual fun PlatformToastEffect(message: String?) {
    // iOS: no-op, could use snackbar in the future
}
