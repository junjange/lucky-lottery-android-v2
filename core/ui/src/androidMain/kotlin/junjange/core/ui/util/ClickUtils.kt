@file:JvmName("ClickUtilsAndroid")

package junjange.core.ui.util

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val MIN_CLICK_INTERVAL = 500L

@Composable
actual fun Modifier.singleClick(onClick: () -> Unit): Modifier {
    var lastClickTime = SystemClock.elapsedRealtime()

    return this.clickable {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime >= MIN_CLICK_INTERVAL) {
            onClick()
            lastClickTime = currentTime
        }
    }
}
