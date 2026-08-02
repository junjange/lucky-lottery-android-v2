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
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.ui.component.LottoLargeTitle
import junjange.feature.setting.resources.Res
import junjange.feature.setting.resources.review_app
import junjange.feature.setting.resources.setting
import junjange.feature.setting.resources.setting_opens_externally
import junjange.feature.setting.resources.setting_section_info
import junjange.feature.setting.resources.setting_section_notification
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
 * 성격이 다른 항목을 섞어 두면 훑어볼 수가 없어서 두 묶음으로 나눴다.
 * 알림(동작을 바꾸는 것) / 정보(읽거나 앱 밖에서 여는 것).
 *
 * @param notificationSection 알림 스위치 두 줄. 알림 권한 요청이 플랫폼마다 달라 이 모듈이 직접
 *   그리지 못하고 셸이 넣어 준다. 예전에는 이걸 위해 화면을 하나 더 열었다.
 */
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
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
        ) {
            // 탭 루트라 큰 제목을 쓴다. 목록과 함께 스크롤되어 올라간다.
            LottoLargeTitle(title = stringResource(Res.string.setting))

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
            SettingExternalLinkItem(
                text = stringResource(Res.string.usage_term),
                onClick = { viewModel.onClickedUsageTerm() },
            )
            // 리뷰도 앱 밖으로 나가는 것이라 '정보'와 같은 묶음에 둔다. 예전에는 '문의'라는
            // 묶음을 따로 뒀는데, 문의가 아니라 스토어 리뷰인 데다 항목이 하나뿐이라
            // 라벨 하나를 위해 24dp를 쓰고 있었다.
            SettingExternalLinkItem(
                text = stringResource(Res.string.review_app),
                onClick = { viewModel.onClickedReview() },
            )

            Spacer(modifier = Modifier.height(LottoSpacing.xl))
        }
    }
}

/**
 * 묶음 이름.
 *
 * 좌우 여백은 화면 공통값(20dp)이 아니라 `ListItem`이 쓰는 16dp에 맞춘다. M3 `ListItem`은
 * 안쪽 여백을 열어 주지 않으므로, 라벨을 20dp로 두면 바로 아래 항목 글자와 4dp씩 어긋난다.
 *
 * 색은 브랜드 그린을 쓰지 않는다. 이 앱에서 그린은 '고른 것 / 주요 동작'의 신호로 쓰고 있어
 * 누를 수 없는 라벨까지 초록이면 신호가 흐려진다.
 */
@Composable
private fun SettingSectionLabel(text: String) {
    Spacer(modifier = Modifier.height(LottoSpacing.base))
    Text(
        modifier = Modifier.padding(horizontal = LottoSpacing.base),
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(LottoSpacing.xs))
}

/**
 * 눌러서 앱 밖으로 나가는 항목.
 *
 * 화살표는 원래 앱 안에서 다음 화면이 열린다는 뜻이라 새 창 아이콘도 후보였지만, 목록에서
 * 오른쪽 끝의 화살표는 '누를 수 있는 줄'이라는 신호로 더 먼저 읽힌다. 앱 밖으로 나간다는 것은
 * 대체 텍스트로만 알린다.
 */
@Composable
private fun SettingExternalLinkItem(
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
                contentDescription = stringResource(Res.string.setting_opens_externally),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
    )
}
