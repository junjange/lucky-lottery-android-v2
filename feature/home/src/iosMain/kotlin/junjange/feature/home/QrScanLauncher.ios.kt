package junjange.feature.home

import androidx.compose.runtime.Composable
import junjange.core.ui.platform.IosAdBridge
import junjange.core.ui.platform.IosQrScannerBridge
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberQrScanAndOpen(): (() -> Unit)? {
    val scan = IosQrScannerBridge.scan ?: return null
    return {
        scan { result ->
            if (result != null) {
                val openScannedUrl = { openUrl(result) }
                IosAdBridge.showInterstitial?.invoke(openScannedUrl) ?: openScannedUrl()
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun openUrl(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    UIApplication.sharedApplication.openURL(nsUrl, options = emptyMap<Any?, Any?>(), completionHandler = null)
}
