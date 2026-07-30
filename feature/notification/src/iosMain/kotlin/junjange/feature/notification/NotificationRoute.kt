package junjange.feature.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

/**
 * NotificationSection의 iOS 래퍼 — UNUserNotificationCenter 알림 인가를 담당한다.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NotificationRoute(
    viewModel: NotificationViewModel,
) {
    var showPermissionSettingsDialog by remember { mutableStateOf(false) }

    fun requestNotification(onGranted: () -> Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.getNotificationSettingsWithCompletionHandler { settings ->
            dispatch_async(dispatch_get_main_queue()) {
                when (settings?.authorizationStatus) {
                    UNAuthorizationStatusAuthorized,
                    UNAuthorizationStatusProvisional,
                    -> onGranted()

                    UNAuthorizationStatusDenied -> showPermissionSettingsDialog = true

                    else ->
                        center.requestAuthorizationWithOptions(
                            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
                        ) { granted, _ ->
                            dispatch_async(dispatch_get_main_queue()) {
                                if (granted) onGranted() else showPermissionSettingsDialog = true
                            }
                        }
                }
            }
        }
    }

    NotificationSection(
        viewModel = viewModel,
        onRequestLottoNotification = {
            requestNotification { viewModel.setLottoNotification(true) }
        },
        onRequestPensionLottoNotification = {
            requestNotification { viewModel.setPensionLottoNotification(true) }
        },
        showPermissionSettingsDialog = showPermissionSettingsDialog,
        onDismissPermissionDialog = { showPermissionSettingsDialog = false },
        onNavigateToSettings = {
            showPermissionSettingsDialog = false
            openAppSettings()
        },
    )
}

private fun openAppSettings() {
    val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString) ?: return
    UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any?>(), completionHandler = null)
}
