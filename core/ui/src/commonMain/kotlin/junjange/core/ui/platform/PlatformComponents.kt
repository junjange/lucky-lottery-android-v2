package junjange.core.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Platform-specific ad banner.
 * Android: AdMob banner, iOS: empty/placeholder
 */
@Composable
expect fun PlatformAdBanner(modifier: Modifier)

/**
 * Platform-specific toast/notification.
 * Android: Toast, iOS: no-op (or could use snackbar)
 */
@Composable
expect fun PlatformToastEffect(message: String?)
