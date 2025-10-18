package com.junjange.presentation.ui.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junjange.presentation.R
import junjange.core.ui.component.LottoSimpleTopBar
import junjange.core.ui.component.LottoSwitchBar

@Composable
fun NotificationScreen(viewModel: NotificationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { LottoSimpleTopBar(titleRes = R.string.notification_title) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LottoSwitchBar(
                textRes = R.string.lotto_notification_title,
                descriptionTextRes = R.string.lotto_notification_description,
                isSwitchedOn = uiState.isLottoNotificationAvailable,
                onSwitchOn = { viewModel.setLottoNotification(true) },
                onSwitchOff = { viewModel.setLottoNotification(false) },
            )

            LottoSwitchBar(
                textRes = R.string.pension_lotto_notification_title,
                descriptionTextRes = R.string.pension_lotto_notification_description,
                isSwitchedOn = uiState.isPensionLottoNotificationAvailable,
                onSwitchOn = { viewModel.setPensionLottoNotification(true) },
                onSwitchOff = { viewModel.setPensionLottoNotification(false) },
            )
        }
    }
}
