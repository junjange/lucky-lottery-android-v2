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

/** 브랜드 프라이머리(시드). 디자인 시스템은 이 색에서 파생한 M3 톤 팔레트만 사용하고, 컴포넌트는 OS 기본을 따른다. */
val BrandPrimary = Color(0xFF30AA5B)

// BrandPrimary(#30AA5B)에서 파생한 M3 톤 (Material Theme Builder 기준 근사값)
val LightColorScheme =
    lightColorScheme(
        primary = BrandPrimary,
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFB2F1C0),
        onPrimaryContainer = Color(0xFF00210D),
        inversePrimary = Color(0xFF97D5A6),
        secondary = Color(0xFF4F6353),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFD2E8D4),
        onSecondaryContainer = Color(0xFF0C1F13),
        tertiary = Color(0xFF3B646F),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBFE9F6),
        onTertiaryContainer = Color(0xFF001F26),
        background = Color(0xFFF6FBF3),
        onBackground = Color(0xFF181D18),
        surface = Color(0xFFF6FBF3),
        onSurface = Color(0xFF181D18),
        surfaceVariant = Color(0xFFDDE5DB),
        onSurfaceVariant = Color(0xFF414942),
        inverseSurface = Color(0xFF2C322D),
        inverseOnSurface = Color(0xFFEDF2E9),
        outline = Color(0xFF717971),
        outlineVariant = Color(0xFFC1C9BF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF0F5ED),
        surfaceContainer = Color(0xFFEAEFE7),
        surfaceContainerHigh = Color(0xFFE4EAE1),
        surfaceContainerHighest = Color(0xFFDFE4DC),
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
        secondaryContainer = Color(0xFF384B3C),
        onSecondaryContainer = Color(0xFFD2E8D4),
        tertiary = Color(0xFFA3CDDA),
        onTertiary = Color(0xFF03363F),
        tertiaryContainer = Color(0xFF224C56),
        onTertiaryContainer = Color(0xFFBFE9F6),
        background = Color(0xFF101510),
        onBackground = Color(0xFFDFE4DC),
        surface = Color(0xFF101510),
        onSurface = Color(0xFFDFE4DC),
        surfaceVariant = Color(0xFF414942),
        onSurfaceVariant = Color(0xFFC1C9BF),
        inverseSurface = Color(0xFFDFE4DC),
        inverseOnSurface = Color(0xFF2C322D),
        outline = Color(0xFF8B938A),
        outlineVariant = Color(0xFF414942),
        surfaceContainerLowest = Color(0xFF0B0F0B),
        surfaceContainerLow = Color(0xFF181D18),
        surfaceContainer = Color(0xFF1C211C),
        surfaceContainerHigh = Color(0xFF262B26),
        surfaceContainerHighest = Color(0xFF313631),
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
