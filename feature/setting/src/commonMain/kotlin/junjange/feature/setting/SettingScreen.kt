package junjange.feature.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import junjange.core.ui.component.LottoButtonBar
import junjange.core.ui.component.LottoSimpleTopBar
import junjange.core.ui.component.LottoTextBar
import junjange.feature.setting.resources.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    navigateToNotification: (lottoNotificationState: Boolean, pensionLottoNotificationState: Boolean) -> Unit,
    onOpenUrl: (String) -> Unit = {},
    onOpenReview: () -> Unit = {},
    versionName: String = "",
) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SettingEffect.NavigateToNotification ->
                    navigateToNotification(
                        effect.lottoNotificationState,
                        effect.pensionLottoNotificationState,
                    )
                is SettingEffect.NavigateToUsageTerm ->
                    onOpenUrl("https://fre2-dom.tistory.com/7")
                is SettingEffect.NavigateToReview ->
                    onOpenReview()
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { LottoSimpleTopBar(titleRes = Res.string.setting) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LottoButtonBar(
                textRes = Res.string.app_notification,
                onClick = { viewModel.onClickedNotification() },
            )
            LottoButtonBar(
                textRes = Res.string.usage_term,
                onClick = { viewModel.onClickedUsageTerm() },
            )
            LottoTextBar(
                text = stringResource(Res.string.version_info),
                subtext = versionName,
            )
            LottoButtonBar(
                textRes = Res.string.review_app,
                onClick = { viewModel.onClickedReview() },
            )
        }
    }
}
