package junjange.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color

val LottoBlack = Color(0xFF1F2128)
val LottoGray = Color(0xFFAAAAAA)
val LottoPurple = Color(0xFF8D70DA)
val LottoYellow = Color(0xFFFBC400)
val LottoWhite = Color(0xFFF9F9F9)
val LottoOrange = Color(0xFFFF8E4F)
val LottoGreen = Color(0xFFB0D840)
val LottoError = Color(0xFFFF7272)
val LottoBlue = Color(0xFF67C8F2)

val Gray900 = Color(0xFF212121)
val Gray800 = Color(0xFF424242)
val Gray700 = Color(0xFF616161)
val Gray600 = Color(0xFF757575)
val Gray500 = Color(0xFF9E9E9E)
val Gray400 = Color(0xFFBDBDBD)
val Gray300 = Color(0xFFE0E0E0)
val Gray200 = Color(0xFFEEEEEE)
val Gray100 = Color(0xFFF5F5F5)
val Gray50 = Color(0xFF7F7F7F)

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val Green = Color(0xFF30AA5B)

/** 브랜드 프라이머리(시드). 디자인 시스템은 이 색에서 파생한 M3 톤 팔레트만 사용하고, 컴포넌트는 OS 기본을 따른다. */
val BrandPrimary = Green

// BrandPrimary(#30AA5B)에서 파생한 M3 톤 (Material Theme Builder 기준 근사값)
val LightColorScheme =
    lightColorScheme(
        primary = BrandPrimary,
        onPrimary = White,
        primaryContainer = Color(0xFFB2F1C0),
        onPrimaryContainer = Color(0xFF00210D),
        secondary = Color(0xFF4F6353),
        onSecondary = White,
        secondaryContainer = Color(0xFFD2E8D4),
        onSecondaryContainer = Color(0xFF0C1F13),
        tertiary = Color(0xFF3B646F),
        onTertiary = White,
        tertiaryContainer = Color(0xFFBFE9F6),
        onTertiaryContainer = Color(0xFF001F26),
        background = Color(0xFFF6FBF3),
        onBackground = Color(0xFF181D18),
        surface = Color(0xFFF6FBF3),
        onSurface = Color(0xFF181D18),
        surfaceVariant = Color(0xFFDDE5DB),
        onSurfaceVariant = Color(0xFF414942),
        outline = Color(0xFF717971),
        surfaceContainerLowest = White,
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
        secondary = Color(0xFFB6CCB8),
        onSecondary = Color(0xFF223527),
        secondaryContainer = Color(0xFF384B3C),
        onSecondaryContainer = Color(0xFFD2E8D4),
        background = Color(0xFF101510),
        onBackground = Color(0xFFDFE4DC),
        surface = Color(0xFF101510),
        onSurface = Color(0xFFDFE4DC),
        surfaceVariant = Color(0xFF414942),
        onSurfaceVariant = Color(0xFFC1C9BF),
        outline = Color(0xFF8B938A),
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
        else -> Gray200
    }

@Stable
class LottoColors(
    lottoBlack: Color,
    lottoWhite: Color,
    lottoGray: Color,
    lottoPurple: Color,
    lottoYellow: Color,
    lottoOrange: Color,
    lottoGreen: Color,
    lottoError: Color,
    lottoBlue: Color,
    gray900: Color,
    gray800: Color,
    gray700: Color,
    gray600: Color,
    gray500: Color,
    gray400: Color,
    gray300: Color,
    gray200: Color,
    gray100: Color,
    gray50: Color,
    black: Color,
    white: Color,
    green: Color,
) {
    var lottoBlack: Color by mutableStateOf(lottoBlack, structuralEqualityPolicy())
        private set
    var lottoWhite: Color by mutableStateOf(lottoWhite, structuralEqualityPolicy())
        private set
    var lottoGray: Color by mutableStateOf(lottoGray, structuralEqualityPolicy())
        private set
    var lottoPurple: Color by mutableStateOf(lottoPurple, structuralEqualityPolicy())
        private set
    var lottoYellow: Color by mutableStateOf(lottoYellow, structuralEqualityPolicy())
        private set
    var lottoOrange: Color by mutableStateOf(lottoOrange, structuralEqualityPolicy())
        private set
    var lottoGreen: Color by mutableStateOf(lottoGreen, structuralEqualityPolicy())
        private set
    var lottoError: Color by mutableStateOf(lottoError, structuralEqualityPolicy())
        private set
    var lottoBlue: Color by mutableStateOf(lottoBlue, structuralEqualityPolicy())
        private set
    var gray900: Color by mutableStateOf(gray900, structuralEqualityPolicy())
        private set
    var gray800: Color by mutableStateOf(gray800, structuralEqualityPolicy())
        private set
    var gray700: Color by mutableStateOf(gray700, structuralEqualityPolicy())
        private set
    var gray600: Color by mutableStateOf(gray600, structuralEqualityPolicy())
        private set
    var gray500: Color by mutableStateOf(gray500, structuralEqualityPolicy())
        private set
    var gray400: Color by mutableStateOf(gray400, structuralEqualityPolicy())
        private set
    var gray300: Color by mutableStateOf(gray300, structuralEqualityPolicy())
        private set
    var gray200: Color by mutableStateOf(gray200, structuralEqualityPolicy())
        private set
    var gray100: Color by mutableStateOf(gray100, structuralEqualityPolicy())
        private set
    var gray50: Color by mutableStateOf(gray50, structuralEqualityPolicy())
        private set
    var black: Color by mutableStateOf(black, structuralEqualityPolicy())
        private set
    var white: Color by mutableStateOf(white, structuralEqualityPolicy())
        private set
    var green: Color by mutableStateOf(green, structuralEqualityPolicy())
        private set

    fun copy(
        lottoBlack: Color = this.lottoBlack,
        lottoWhite: Color = this.lottoWhite,
        lottoGray: Color = this.lottoGray,
        lottoPurple: Color = this.lottoPurple,
        lottoYellow: Color = this.lottoYellow,
        lottoOrange: Color = this.lottoOrange,
        lottoGreen: Color = this.lottoGreen,
        lottoError: Color = this.lottoError,
        lottoBlue: Color = this.lottoBlue,
        gray900: Color = this.gray900,
        gray800: Color = this.gray800,
        gray700: Color = this.gray700,
        gray600: Color = this.gray600,
        gray500: Color = this.gray500,
        gray400: Color = this.gray400,
        gray300: Color = this.gray300,
        gray200: Color = this.gray200,
        gray100: Color = this.gray100,
        gray50: Color = this.gray50,
        black: Color = this.black,
        white: Color = this.white,
        green: Color = this.green,
    ) = LottoColors(
        lottoBlack = lottoBlack,
        lottoWhite = lottoWhite,
        lottoGray = lottoGray,
        lottoPurple = lottoPurple,
        lottoYellow = lottoYellow,
        lottoOrange = lottoOrange,
        lottoGreen = lottoGreen,
        lottoError = lottoError,
        lottoBlue = lottoBlue,
        gray900 = gray900,
        gray800 = gray800,
        gray700 = gray700,
        gray600 = gray600,
        gray500 = gray500,
        gray400 = gray400,
        gray300 = gray300,
        gray200 = gray200,
        gray100 = gray100,
        gray50 = gray50,
        black = black,
        white = white,
        green = green,
    )
}

fun lightColors(
    lottoBlack: Color = LottoBlack,
    lottoWhite: Color = LottoWhite,
    lottoGray: Color = LottoGray,
    lottoPurple: Color = LottoPurple,
    lottoYellow: Color = LottoYellow,
    lottoOrange: Color = LottoOrange,
    lottoGreen: Color = LottoGreen,
    lottoError: Color = LottoError,
    lottoBlue: Color = LottoBlue,
    gray900: Color = Gray900,
    gray800: Color = Gray800,
    gray700: Color = Gray700,
    gray600: Color = Gray600,
    gray500: Color = Gray500,
    gray400: Color = Gray400,
    gray300: Color = Gray300,
    gray200: Color = Gray200,
    gray100: Color = Gray100,
    gray50: Color = Gray50,
    black: Color = Black,
    white: Color = White,
    green: Color = Green,
) = LottoColors(
    lottoBlack = lottoBlack,
    lottoWhite = lottoWhite,
    lottoGray = lottoGray,
    lottoPurple = lottoPurple,
    lottoYellow = lottoYellow,
    lottoOrange = lottoOrange,
    lottoGreen = lottoGreen,
    lottoError = lottoError,
    lottoBlue = lottoBlue,
    gray900 = gray900,
    gray800 = gray800,
    gray700 = gray700,
    gray600 = gray600,
    gray500 = gray500,
    gray400 = gray400,
    gray300 = gray300,
    gray200 = gray200,
    gray100 = gray100,
    gray50 = gray50,
    black = black,
    white = white,
    green = green,
)

internal val LocalColors = staticCompositionLocalOf { lightColors() }
