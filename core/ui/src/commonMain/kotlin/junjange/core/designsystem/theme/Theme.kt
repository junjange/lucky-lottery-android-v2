package junjange.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

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

    val typography = rememberLottoTypography()
    val legacyTypography = remember(typography) { typography.toLegacy() }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = typography,
        shapes = LottoShapes,
    ) {
        CompositionLocalProvider(
            LocalTypography provides legacyTypography,
            content = content,
        )
    }
}

@Composable
expect fun PlatformThemeEffect(darkTheme: Boolean)

object LottoTheme {
    /**
     * 구 타이포 이름을 쓰는 화면을 위한 브리지. 새 코드는 `MaterialTheme.typography`를 쓴다.
     * 자세한 내용은 [LottoTypography].
     */
    val typography: LottoTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    /** 간격 토큰. `LottoTheme.spacing.base` 처럼 쓴다. */
    val spacing: LottoSpacing
        get() = LottoSpacing
}
