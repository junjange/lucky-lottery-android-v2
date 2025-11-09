package junjange.feature.notification

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.base.BaseActivity

@AndroidEntryPoint
class NotificationActivity : BaseActivity() {
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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

            LottoTheme {
                NotificationScreen(
                    viewModel = viewModel,
                    finish = { finish() },
                    onRequestLottoNotification = {
                        if (checkNotificationPermission()) {
                            viewModel.setLottoNotification(true)
                        } else {
                            pendingNotificationType = NotificationType.LOTTO
                            if (shouldShowPermissionRationale()) {
                                requestNotificationPermission(notificationPermissionLauncher)
                            } else {
                                showPermissionSettingsDialog = true
                            }
                        }
                    },
                    onRequestPensionLottoNotification = {
                        if (checkNotificationPermission()) {
                            viewModel.setPensionLottoNotification(true)
                        } else {
                            pendingNotificationType = NotificationType.PENSION_LOTTO
                            if (shouldShowPermissionRationale()) {
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
                        openAppSettings()
                    },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkNotificationPermission()) {
            viewModel.setLottoNotification(false)
            viewModel.setPensionLottoNotification(false)
        }
    }

    private fun checkNotificationPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

    private fun shouldShowPermissionRationale(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            false
        }

    private fun requestNotificationPermission(launcher: androidx.activity.result.ActivityResultLauncher<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun openAppSettings() {
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
        startActivity(intent)
    }

    enum class NotificationType {
        LOTTO,
        PENSION_LOTTO,
    }

    companion object {
        const val EXTRA_KEY_LOTTO_NOTIFICATION_STATE = "EXTRA_KEY_LOTTO_NOTIFICATION_STATE"
        const val EXTRA_KEY_PENSION_LOTTO_NOTIFICATION_STATE =
            "EXTRA_KEY_PENSION_LOTTO_NOTIFICATION_STATE"

        fun startActivity(
            context: Context,
            lottoNotificationState: Boolean,
            pensionLottoNotificationState: Boolean,
        ) {
            val intent =
                Intent(context, NotificationActivity::class.java)
                    .putExtra(EXTRA_KEY_LOTTO_NOTIFICATION_STATE, lottoNotificationState)
                    .putExtra(
                        EXTRA_KEY_PENSION_LOTTO_NOTIFICATION_STATE,
                        pensionLottoNotificationState,
                    )
            context.startActivity(intent)
        }
    }
}
