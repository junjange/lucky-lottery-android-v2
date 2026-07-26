package junjange.core.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Platform-specific ad banner.
 * Android: AdMob banner, iOS: empty/placeholder
 */
@Composable
expect fun PlatformAdBanner(modifier: Modifier)
