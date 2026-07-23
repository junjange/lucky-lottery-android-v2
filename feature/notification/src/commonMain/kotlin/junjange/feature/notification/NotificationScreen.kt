package junjange.feature.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.collectAsState
import junjange.core.designsystem.components.dialog.LottoTwoButtonDialog
import junjange.core.designsystem.theme.Gray800
import junjange.core.designsystem.theme.Gray900
import junjange.core.designsystem.theme.Green
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.White
import junjange.core.ui.component.LottoSimpleTopBar
import junjange.core.ui.component.LottoSwitchBar
import junjange.core.ui.resources.Res as CoreRes
import junjange.core.ui.resources.*
import junjange.feature.notification.resources.*

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    finish: () -> Unit,
    onRequestLottoNotification: () -> Unit,
    onRequestPensionLottoNotification: () -> Unit,
    showPermissionSettingsDialog: Boolean,
    onDismissPermissionDialog: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            LottoSimpleTopBar(
                titleRes = Res.string.notification_title,
                onBack = finish,
                backIconRes = CoreRes.drawable.ic_chevron_left,
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LottoSwitchBar(
                textRes = Res.string.lotto_notification_title,
                descriptionTextRes = Res.string.lotto_notification_description,
                isSwitchedOn = uiState.isLottoNotificationAvailable,
                onSwitchOn = onRequestLottoNotification,
                onSwitchOff = { viewModel.setLottoNotification(false) },
            )

            LottoSwitchBar(
                textRes = Res.string.pension_lotto_notification_title,
                descriptionTextRes = Res.string.pension_lotto_notification_description,
                isSwitchedOn = uiState.isPensionLottoNotificationAvailable,
                onSwitchOn = onRequestPensionLottoNotification,
                onSwitchOff = { viewModel.setPensionLottoNotification(false) },
            )
        }
    }

    if (showPermissionSettingsDialog) {
        PermissionSettingsDialog(
            onDismiss = onDismissPermissionDialog,
            onNavigateToSettings = onNavigateToSettings,
        )
    }
}

@Composable
private fun PermissionSettingsDialog(
    onDismiss: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    LottoTwoButtonDialog(
        title = stringResource(Res.string.permission_settings_dialog_title),
        content = stringResource(Res.string.permission_settings_dialog_message),
        confirmText = stringResource(Res.string.permission_settings_dialog_confirm),
        cancelText = stringResource(Res.string.permission_settings_dialog_dismiss),
        onConfirm = onNavigateToSettings,
        onCancel = onDismiss,
    )
}
