package junjange.feature.setting

import androidx.compose.runtime.Composable

/**
 * 설정 화면의 플랫폼 동작 모음 — URL 열기, 스토어 리뷰, 앱 버전.
 */
class SettingActions(
    val openUrl: (String) -> Unit,
    val openReview: () -> Unit,
    val versionName: String,
)

@Composable
expect fun rememberSettingActions(): SettingActions
