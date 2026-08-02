package junjange.feature.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import junjange.core.designsystem.components.dialog.LottoTwoButtonDialog
import junjange.feature.notification.resources.Res
import junjange.feature.notification.resources.lotto_notification_description
import junjange.feature.notification.resources.lotto_notification_title
import junjange.feature.notification.resources.pension_lotto_notification_description
import junjange.feature.notification.resources.pension_lotto_notification_title
import junjange.feature.notification.resources.permission_settings_dialog_confirm
import junjange.feature.notification.resources.permission_settings_dialog_dismiss
import junjange.feature.notification.resources.permission_settings_dialog_message
import junjange.feature.notification.resources.permission_settings_dialog_title
import org.jetbrains.compose.resources.stringResource

/**
 * 알림 스위치 두 줄. 설정 화면의 '알림' 묶음 안에 그대로 들어간다.
 *
 * 예전에는 이것만을 위한 별도 화면이 있었다. 설정에서 '알림 설정'을 눌러 화면을 하나 더 열면
 * 스위치 두 개가 나오는 구조였는데, 두 줄을 보려고 화면을 옮길 이유가 없다.
 * 설정 목록에 바로 놓으면 켜고 끄는 데 탭 두 번이 줄고, 지금 켜져 있는지도 설정에서 바로 보인다.
 *
 * 켜고 끄는 것은 스위치로만 한다. 행 전체를 누를 수 있게 두면 설명을 읽으려고 짚었을 때도
 * 알림이 켜지고, 그 순간 권한 요청 팝업까지 뜬다. 되돌리려면 한 번 더 눌러야 하는 동작이라
 * 과녁을 좁게 잡는 편이 낫다. 행 높이는 `ListItem`이 주는 72dp라 스위치 자체는 넉넉히 눌린다.
 */
@Composable
fun NotificationSection(
    viewModel: NotificationViewModel,
    onRequestLottoNotification: () -> Unit,
    onRequestPensionLottoNotification: () -> Unit,
    showPermissionSettingsDialog: Boolean,
    onDismissPermissionDialog: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column {
        NotificationToggleItem(
            title = stringResource(Res.string.lotto_notification_title),
            description = stringResource(Res.string.lotto_notification_description),
            isOn = uiState.isLottoNotificationAvailable,
            onToggle = { isOn ->
                if (isOn) onRequestLottoNotification() else viewModel.setLottoNotification(false)
            },
        )

        NotificationToggleItem(
            title = stringResource(Res.string.pension_lotto_notification_title),
            description = stringResource(Res.string.pension_lotto_notification_description),
            isOn = uiState.isPensionLottoNotificationAvailable,
            onToggle = { isOn ->
                if (isOn) {
                    onRequestPensionLottoNotification()
                } else {
                    viewModel.setPensionLottoNotification(false)
                }
            },
        )
    }

    if (showPermissionSettingsDialog) {
        LottoTwoButtonDialog(
            title = stringResource(Res.string.permission_settings_dialog_title),
            content = stringResource(Res.string.permission_settings_dialog_message),
            confirmText = stringResource(Res.string.permission_settings_dialog_confirm),
            cancelText = stringResource(Res.string.permission_settings_dialog_dismiss),
            onConfirm = onNavigateToSettings,
            onCancel = onDismissPermissionDialog,
        )
    }
}

@Composable
private fun NotificationToggleItem(
    title: String,
    description: String,
    isOn: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = { Text(text = title) },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingContent = { Switch(checked = isOn, onCheckedChange = onToggle) },
    )
}
