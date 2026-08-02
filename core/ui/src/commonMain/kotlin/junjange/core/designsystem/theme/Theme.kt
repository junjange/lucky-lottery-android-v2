package junjange.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * 앱 테마.
 *
 * 커스텀 컴포넌트를 만들지 않고 OS 기본 컴포넌트를 쓰는 대신,
 * 색·타이포·모서리 세 가지 토큰만 M3 슬롯에 주입해 룩을 만든다.
 * 간격은 컴포넌트가 아니라 화면이 정하므로 [LottoSpacing]에서 직접 가져다 쓴다.
 */
@Composable
fun LottoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    PlatformThemeEffect(darkTheme)

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = rememberLottoTypography(),
        shapes = LottoShapes,
        content = content,
    )
}

@Composable
expect fun PlatformThemeEffect(darkTheme: Boolean)

object LottoTheme {
    /** 간격 토큰. `LottoTheme.spacing.base` 처럼 쓴다. */
    val spacing: LottoSpacing
        get() = LottoSpacing
}
