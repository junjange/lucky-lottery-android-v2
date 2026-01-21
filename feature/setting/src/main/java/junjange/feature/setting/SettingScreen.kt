package junjange.feature.setting

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import org.koin.androidx.compose.koinViewModel
import junjange.core.ui.component.LottoButtonBar
import junjange.core.ui.component.LottoSimpleTopBar
import junjange.core.ui.component.LottoTextBar
import junjange.feature.setting.SettingEffect.NavigateToUsageTerm
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = koinViewModel(),
    navigateToNotification: (lottoNotificationState: Boolean, pensionLottoNotificationState: Boolean) -> Unit,
) {
    val context = LocalContext.current

    val usageTermUri = "https://fre2-dom.tistory.com/7"
    val intent =
        Intent(
            Intent.ACTION_VIEW,
            usageTermUri.toUri(),
        )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SettingEffect.NavigateToNotification ->
                    navigateToNotification(
                        effect.lottoNotificationState,
                        effect.pensionLottoNotificationState,
                    )

                is NavigateToUsageTerm -> context.startActivity(intent)

                is SettingEffect.NavigateToReview -> openPlayStoreReviewFallback(context)
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { LottoSimpleTopBar(titleRes = R.string.setting) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LottoButtonBar(
                textRes = R.string.app_notification,
                onClick = { viewModel.onClickedNotification() },
            )
            LottoButtonBar(
                textRes = R.string.usage_term,
                onClick = { viewModel.onClickedUsageTerm() },
            )
            LottoTextBar(
                text = stringResource(R.string.version_info),
                subtext = BuildConfig.VERSION_NAME,
            )
            LottoButtonBar(
                textRes = R.string.review_app,
                onClick = { viewModel.onClickedReview() },
            )
        }
    }
}

fun openPlayStoreReviewFallback(context: Context) {
    val pkg = context.packageName
    val market =
        Intent(Intent.ACTION_VIEW, "market://details?id=$pkg".toUri())
            .apply { setPackage("com.android.vending") }
    val web =
        Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=$pkg".toUri(),
        )
    try {
        context.startActivity(market)
    } catch (_: ActivityNotFoundException) {
        context.startActivity(web)
    }
}
