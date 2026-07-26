package junjange.core.notification

import android.app.Activity
import androidx.annotation.DrawableRes

data class NotificationConfig(
    val mainActivityClass: Class<out Activity>,
    @DrawableRes val appIconRes: Int,
)
