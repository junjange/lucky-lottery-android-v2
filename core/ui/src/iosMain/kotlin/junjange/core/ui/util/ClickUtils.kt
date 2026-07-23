package junjange.core.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

private const val MIN_CLICK_INTERVAL = 500L

@Composable
actual fun Modifier.singleClick(onClick: () -> Unit): Modifier {
    var lastClickTime = (NSDate().timeIntervalSince1970 * 1000).toLong()

    return this.clickable {
        val currentTime = (NSDate().timeIntervalSince1970 * 1000).toLong()
        if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
            onClick()
            lastClickTime = currentTime
        }
    }
}
