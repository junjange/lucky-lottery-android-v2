package junjange.core.ui.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformAdBanner(modifier: Modifier) {
    // No ads on iOS for now
    Box(modifier = modifier)
}

@Composable
actual fun PlatformToastEffect(message: String?) {
    // iOS: no-op, could use snackbar in the future
}
