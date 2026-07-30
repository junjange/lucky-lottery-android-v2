package junjange.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * 타이포 스케일.
 *
 * OS 기본 컴포넌트가 그대로 브랜드 룩을 갖도록 M3 `Typography` 슬롯에 직접 정의한다.
 * Button/ListItem/TopAppBar 등은 이 슬롯을 읽으므로 컴포넌트별 스타일 지정이 필요 없다.
 *
 * 규칙
 * - weight는 번들한 네 가지(400/500/600/700)만 쓴다.
 * - 크기가 클수록 자간을 더 좁힌다. 큰 글자는 기본 자간이 헐렁해 보인다.
 * - lineHeight는 본문 1.5배, 제목 1.3배 근처를 유지한다.
 */
private val CenteredLineHeight =
    LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    )

private fun scaleStyle(
    fontFamily: FontFamily,
    size: Int,
    lineHeight: Int,
    weight: FontWeight,
    tracking: Float,
) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = tracking.em,
    lineHeightStyle = CenteredLineHeight,
)

fun lottoTypography(fontFamily: FontFamily): Typography =
    Typography(
        // display — 당첨금처럼 화면의 주인공이 되는 숫자
        displayLarge = scaleStyle(fontFamily, 40, 48, FontWeight.Bold, -0.03f),
        displayMedium = scaleStyle(fontFamily, 32, 40, FontWeight.Bold, -0.03f),
        displaySmall = scaleStyle(fontFamily, 28, 36, FontWeight.Bold, -0.025f),
        // headline — 화면 대제목
        headlineLarge = scaleStyle(fontFamily, 24, 32, FontWeight.Bold, -0.02f),
        headlineMedium = scaleStyle(fontFamily, 22, 30, FontWeight.Bold, -0.02f),
        headlineSmall = scaleStyle(fontFamily, 20, 28, FontWeight.Bold, -0.02f),
        // title — 카드·섹션 제목
        titleLarge = scaleStyle(fontFamily, 18, 26, FontWeight.SemiBold, -0.02f),
        titleMedium = scaleStyle(fontFamily, 16, 24, FontWeight.SemiBold, -0.015f),
        titleSmall = scaleStyle(fontFamily, 15, 22, FontWeight.SemiBold, -0.01f),
        // body — 본문
        bodyLarge = scaleStyle(fontFamily, 17, 26, FontWeight.Normal, -0.01f),
        bodyMedium = scaleStyle(fontFamily, 15, 23, FontWeight.Normal, -0.01f),
        bodySmall = scaleStyle(fontFamily, 13, 20, FontWeight.Normal, -0.005f),
        // label — 버튼·보조 라벨
        labelLarge = scaleStyle(fontFamily, 16, 22, FontWeight.SemiBold, -0.01f),
        labelMedium = scaleStyle(fontFamily, 13, 18, FontWeight.Medium, 0f),
        labelSmall = scaleStyle(fontFamily, 11, 16, FontWeight.Medium, 0f),
    )

/**
 * 구 타이포 이름(headline1, body2 …)을 쓰는 화면이 남아 있는 동안만 유지하는 브리지.
 * 화면을 옮길 때마다 `MaterialTheme.typography`로 갈아타고, 마지막 참조가 사라지면 이 타입을 지운다.
 */
@Immutable
data class LottoTypography(
    val headline1: TextStyle,
    val headline2: TextStyle,
    val headline3: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,
    val body4: TextStyle,
    val caption1: TextStyle,
    val caption2: TextStyle,
    val caption3: TextStyle,
)

internal fun Typography.toLegacy(): LottoTypography =
    LottoTypography(
        headline1 = headlineLarge,
        headline2 = headlineMedium,
        headline3 = headlineSmall,
        body1 = bodyLarge,
        body2 = bodyMedium,
        body3 = bodySmall,
        body4 = labelSmall,
        caption1 = titleMedium,
        caption2 = labelMedium,
        caption3 = labelSmall,
    )

internal val LocalTypography =
    staticCompositionLocalOf { lottoTypography(FontFamily.Default).toLegacy() }

/** 테마가 한 번만 만들어 두는 타이포 인스턴스. 폰트 리소스 로딩이 매 컴포지션 반복되지 않게 한다. */
@Composable
internal fun rememberLottoTypography(): Typography {
    val fontFamily = pretendardFontFamily()
    return remember(fontFamily) { lottoTypography(fontFamily) }
}
