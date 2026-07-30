package junjange.feature.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import junjange.core.designsystem.theme.LottoSpacing
import junjange.feature.setting.resources.Res
import junjange.feature.setting.resources.review_app
import junjange.feature.setting.resources.setting
import junjange.feature.setting.resources.setting_section_info
import junjange.feature.setting.resources.setting_section_notification
import junjange.feature.setting.resources.setting_section_support
import junjange.feature.setting.resources.usage_term
import junjange.feature.setting.resources.version_info
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource

/** 이용 약관 주소. 화면 코드에 흩어져 있으면 바뀔 때 찾기 어려워 한곳에 둔다. */
private const val USAGE_TERM_URL = "https://fre2-dom.tistory.com/7"

/**
 * 설정.
 *
 * 예전에는 커스텀 `LottoButtonBar`/`LottoTextBar`를 8dp 간격으로 평평하게 늘어놓았다.
 * 두 컴포넌트의 세로 패딩이 12dp와 8dp로 달라 행 높이가 46dp와 38dp로 어긋났고,
 * 둘 다 48dp 최소 터치 크기에 못 미쳤다. M3 `ListItem`은 56dp를 기본으로 준다.
 *
 * 성격이 다른 항목을 섞어 두면 훑어볼 수가 없어서 세 묶음으로 나눴다.
 * 알림(동작을 바꾸는 것) / 정보(읽는 것) / 문의(앱 밖으로 나가는 것).
 *
 * @param notificationSection 알림 스위치 두 줄. 알림 권한 요청이 플랫폼마다 달라 이 모듈이 직접
 *   그리지 못하고 셸이 넣어 준다. 예전에는 이걸 위해 화면을 하나 더 열었다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    notificationSection: @Composable () -> Unit,
    onOpenUrl: (String) -> Unit = {},
    onOpenReview: () -> Unit = {},
    versionName: String = "",
) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SettingEffect.NavigateToUsageTerm -> onOpenUrl(USAGE_TERM_URL)
                is SettingEffect.NavigateToReview -> onOpenReview()
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { TopAppBar(title = { Text(text = stringResource(Res.string.setting)) }) },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
        ) {
            SettingSectionLabel(text = stringResource(Res.string.setting_section_notification))
            notificationSection()

            SettingSectionLabel(text = stringResource(Res.string.setting_section_info))
            // 지금 쓰는 앱이 몇 버전인지가 약관보다 먼저 궁금해지는 정보라 위에 둔다.
            ListItem(
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                headlineContent = { Text(text = stringResource(Res.string.version_info)) },
                // 누를 수 없는 항목이라 화살표를 두지 않는다. 값만 오른쪽에 보인다.
                trailingContent = {
                    Text(
                        text = versionName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
            SettingNavigationItem(
                text = stringResource(Res.string.usage_term),
                onClick = { viewModel.onClickedUsageTerm() },
            )

            SettingSectionLabel(text = stringResource(Res.string.setting_section_support))
            SettingNavigationItem(
                text = stringResource(Res.string.review_app),
                onClick = { viewModel.onClickedReview() },
            )

            Spacer(modifier = Modifier.height(LottoSpacing.xl))
        }
    }
}

/** 묶음 이름. 위쪽 간격을 넉넉히 두어 앞 묶음과 끊어 읽히게 한다. */
@Composable
private fun SettingSectionLabel(text: String) {
    Spacer(modifier = Modifier.height(LottoSpacing.base))
    Text(
        modifier = Modifier.padding(horizontal = LottoSpacing.screenHorizontal),
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
    )
    Spacer(modifier = Modifier.height(LottoSpacing.xs))
}

/** 눌러서 다른 화면·앱 밖으로 나가는 항목. 화살표로 이동한다는 것을 알린다. */
@Composable
private fun SettingNavigationItem(
    text: String,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = { Text(text = text) },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
    )
}
