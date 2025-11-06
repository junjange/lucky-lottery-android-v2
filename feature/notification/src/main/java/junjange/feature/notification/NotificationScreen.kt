package junjange.feature.notification

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import junjange.core.ui.component.LottoSimpleTopBar
import junjange.core.ui.component.LottoSwitchBar
import junjange.feature.notification.NotificationActivity.NotificationType

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    finish: () -> Unit,
    onRequestLottoNotification: () -> Unit,
    onRequestPensionLottoNotification: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            LottoSimpleTopBar(
                titleRes = R.string.notification_title,
                onBack = finish,
                backIconRes = junjange.core.ui.R.drawable.ic_chevron_left,
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LottoSwitchBar(
                textRes = R.string.lotto_notification_title,
                descriptionTextRes = R.string.lotto_notification_description,
                isSwitchedOn = uiState.isLottoNotificationAvailable,
                onSwitchOn = onRequestLottoNotification,
                onSwitchOff = { viewModel.setLottoNotification(false) },
            )

            LottoSwitchBar(
                textRes = R.string.pension_lotto_notification_title,
                descriptionTextRes = R.string.pension_lotto_notification_description,
                isSwitchedOn = uiState.isPensionLottoNotificationAvailable,
                onSwitchOn = onRequestPensionLottoNotification,
                onSwitchOff = { viewModel.setPensionLottoNotification(false) },
            )
        }
    }
}
