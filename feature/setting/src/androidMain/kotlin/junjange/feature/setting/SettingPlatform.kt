package junjange.feature.setting

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun openPlayStoreReviewFallback(context: Context) {
    val pkg = context.packageName
    val market =
        Intent(Intent.ACTION_VIEW, "market://details?id=$pkg".toUri())
            .apply { setPackage("com.android.vending") }
    val web =
        Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=$pkg".toUri(),
        )
    try {
        context.startActivity(market)
    } catch (_: ActivityNotFoundException) {
        context.startActivity(web)
    }
}
