package junjange.feature.setting

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
actual fun rememberSettingActions(): SettingActions {
    val context = LocalContext.current
    return remember(context) {
        val versionName =
            runCatching {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            }.getOrNull() ?: ""

        SettingActions(
            openUrl = { url ->
                context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
            },
            openReview = { openPlayStoreReviewFallback(context) },
            versionName = versionName,
        )
    }
}
