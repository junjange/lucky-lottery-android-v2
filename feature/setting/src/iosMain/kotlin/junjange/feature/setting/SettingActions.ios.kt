package junjange.feature.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.StoreKit.SKStoreReviewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindowScene

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberSettingActions(): SettingActions =
    remember {
        SettingActions(
            openUrl = { url ->
                NSURL.URLWithString(url)?.let {
                    UIApplication.sharedApplication.openURL(it, options = emptyMap<Any?, Any?>(), completionHandler = null)
                }
            },
            openReview = {
                val scene =
                    UIApplication.sharedApplication.connectedScenes
                        .filterIsInstance<UIWindowScene>()
                        .firstOrNull()
                scene?.let { SKStoreReviewController.requestReviewInScene(it) }
            },
            versionName =
                NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "",
        )
    }
