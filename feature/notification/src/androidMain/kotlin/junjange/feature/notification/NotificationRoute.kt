package junjange.feature.notification

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleResumeEffect

private enum class NotificationType {
    LOTTO,
    PENSION_LOTTO,
}

/**
 * NotificationSection의 Android 래퍼 — POST_NOTIFICATIONS 런타임 권한 처리를 담당한다.
 */
@Composable
actual fun NotificationRoute(
    viewModel: NotificationViewModel,
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var pendingNotificationType by remember { mutableStateOf<NotificationType?>(null) }
    var showPermissionSettingsDialog by remember { mutableStateOf(false) }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                when (pendingNotificationType) {
                    NotificationType.LOTTO -> viewModel.setLottoNotification(true)
                    NotificationType.PENSION_LOTTO -> viewModel.setPensionLottoNotification(true)
                    null -> {}
                }
            }
            pendingNotificationType = null
        }

    LifecycleResumeEffect(Unit) {
        if (!checkNotificationPermission(context)) {
            viewModel.setLottoNotification(false)
            viewModel.setPensionLottoNotification(false)
        }
        onPauseOrDispose {}
    }

    NotificationSection(
        viewModel = viewModel,
        onRequestLottoNotification = {
            if (checkNotificationPermission(context)) {
                viewModel.setLottoNotification(true)
            } else {
                pendingNotificationType = NotificationType.LOTTO
                if (shouldShowPermissionRationale(activity)) {
                    requestNotificationPermission(notificationPermissionLauncher)
                } else {
                    showPermissionSettingsDialog = true
                }
            }
        },
        onRequestPensionLottoNotification = {
            if (checkNotificationPermission(context)) {
                viewModel.setPensionLottoNotification(true)
            } else {
                pendingNotificationType = NotificationType.PENSION_LOTTO
                if (shouldShowPermissionRationale(activity)) {
                    requestNotificationPermission(notificationPermissionLauncher)
                } else {
                    showPermissionSettingsDialog = true
                }
            }
        },
        showPermissionSettingsDialog = showPermissionSettingsDialog,
        onDismissPermissionDialog = { showPermissionSettingsDialog = false },
        onNavigateToSettings = {
            showPermissionSettingsDialog = false
            openAppSettings(context)
        },
    )
}

private fun checkNotificationPermission(context: Context): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

private fun shouldShowPermissionRationale(activity: android.app.Activity?): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) ?: false
    } else {
        false
    }

private fun requestNotificationPermission(launcher: ActivityResultLauncher<String>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

private fun openAppSettings(context: Context) {
    val intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    context.startActivity(intent)
}
