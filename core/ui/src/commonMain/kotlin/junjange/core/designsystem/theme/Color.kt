package junjange.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// 로또 볼 도메인 컬러 — 실물 복권의 볼 색이므로 테마(라이트/다크)와 무관하게 고정
val LottoBlack = Color(0xFF1F2128)
val LottoGray = Color(0xFFAAAAAA)
val LottoPurple = Color(0xFF8D70DA)
val LottoYellow = Color(0xFFFBC400)
val LottoOrange = Color(0xFFFF8E4F)
val LottoGreen = Color(0xFFB0D840)
val LottoError = Color(0xFFFF7272)
val LottoBlue = Color(0xFF67C8F2)

/** 번호 미지정 볼의 중립 색 */
val BallNeutral = Color(0xFFEEEEEE)

/** 브랜드 프라이머리. 컴포넌트는 OS 기본을 따르고 색만 브랜드 팔레트로 주입한다. */
val BrandPrimary = Color(0xFF30AA5B)

// 기존 브랜드 팔레트(흰 배경/검정 텍스트/그레이 톤/그린·블루 포인트)를 M3 역할에 매핑.
// 배경·서피스는 기존 룩 그대로, 포인트만 브랜드 컬러를 쓴다.
val LightColorScheme =
    lightColorScheme(
        primary = BrandPrimary,
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFB7EFC4),
        onPrimaryContainer = Color(0xFF00210D),
        inversePrimary = Color(0xFF97D5A6),
        secondary = Color(0xFF4F6353),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFDCEFE0),
        onSecondaryContainer = Color(0xFF0C1F13),
        tertiary = Color(0xFF00668A),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBFE9F6),
        onTertiaryContainer = Color(0xFF001F26),
        background = Color(0xFFF9F9F9),
        onBackground = Color(0xFF1F2128),
        surface = Color(0xFFF9F9F9),
        onSurface = Color(0xFF1F2128),
        surfaceVariant = Color(0xFFF5F5F5),
        onSurfaceVariant = Color(0xFF757575),
        inverseSurface = Color(0xFF313033),
        inverseOnSurface = Color(0xFFF4F4F4),
        outline = Color(0xFF9E9E9E),
        outlineVariant = Color(0xFFBDBDBD),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFFFFFF),
        surfaceContainer = Color(0xFFF5F5F5),
        surfaceContainerHigh = Color(0xFFEEEEEE),
        surfaceContainerHighest = Color(0xFFE0E0E0),
    )

val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFF97D5A6),
        onPrimary = Color(0xFF00391B),
        primaryContainer = Color(0xFF0F5229),
        onPrimaryContainer = Color(0xFFB2F1C0),
        inversePrimary = BrandPrimary,
        secondary = Color(0xFFB6CCB8),
        onSecondary = Color(0xFF223527),
        secondaryContainer = Color(0xFF3A4A3E),
        onSecondaryContainer = Color(0xFFD2E8D4),
        tertiary = Color(0xFF85CFF1),
        onTertiary = Color(0xFF00344A),
        tertiaryContainer = Color(0xFF004C69),
        onTertiaryContainer = Color(0xFFBFE9F6),
        background = Color(0xFF141414),
        onBackground = Color(0xFFE4E4E4),
        surface = Color(0xFF141414),
        onSurface = Color(0xFFE4E4E4),
        surfaceVariant = Color(0xFF3F3F3F),
        onSurfaceVariant = Color(0xFFC6C6C6),
        inverseSurface = Color(0xFFE4E4E4),
        inverseOnSurface = Color(0xFF313131),
        outline = Color(0xFF8F8F8F),
        outlineVariant = Color(0xFF3F3F3F),
        surfaceContainerLowest = Color(0xFF0E0E0E),
        surfaceContainerLow = Color(0xFF1B1B1B),
        surfaceContainer = Color(0xFF1F1F1F),
        surfaceContainerHigh = Color(0xFF2A2A2A),
        surfaceContainerHighest = Color(0xFF353535),
    )

val lotteryColors =
    listOf(
        LottoGray,
        LottoError,
        LottoOrange,
        LottoYellow,
        LottoBlue,
        LottoPurple,
        LottoBlack,
    )

fun Int?.toLotteryColor(): Color =
    when (this) {
        in 1..10 -> LottoYellow
        in 11..20 -> LottoBlue
        in 21..30 -> LottoError
        in 31..40 -> LottoGray
        in 41..45 -> LottoGreen
        else -> BallNeutral
    }
