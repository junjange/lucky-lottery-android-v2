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

/** 다크 모드용 브랜드 톤. 어두운 배경에서 대비를 확보하기 위해 밝게 조정한 값. */
val DarkBrandPrimary = Color(0xFF97D5A6)

// 뉴트럴 스케일.
// 채도를 0으로 두지 않고 파랑을 약간 섞은 회색을 쓴다. 순회색은 값이 낮아질수록 탁해 보인다.
private val Grey50 = Color(0xFFF9FAFB)
private val Grey100 = Color(0xFFF2F4F6)
private val Grey200 = Color(0xFFE5E8EB)
private val Grey300 = Color(0xFFD1D6DB)
private val Grey400 = Color(0xFFB0B8C1)
private val Grey500 = Color(0xFF8B95A1)
private val Grey600 = Color(0xFF6B7684)
private val Grey700 = Color(0xFF4E5968)
private val Grey800 = Color(0xFF333D4B)
private val Grey900 = Color(0xFF191F28)

// 뉴트럴만으로 위계를 만들고 색은 브랜드 그린 한 가지만 쓴다.
// 서피스 계열은 "회색 캔버스 + 흰 카드" 구조 — surface가 배경, surfaceContainerLowest/Low가 카드다.
val LightColorScheme =
    lightColorScheme(
        primary = BrandPrimary,
        onPrimary = Color(0xFFFFFFFF),
        // 컨테이너 계열도 같은 브랜드 초록을 쓴다. FAB(primaryContainer)와
        // 하단 탭 선택 인디케이터(secondaryContainer)가 연한 톤으로 갈리지 않게 하기 위함.
        primaryContainer = BrandPrimary,
        onPrimaryContainer = Color(0xFFFFFFFF),
        inversePrimary = DarkBrandPrimary,
        secondary = BrandPrimary,
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = BrandPrimary,
        onSecondaryContainer = Color(0xFFFFFFFF),
        tertiary = Grey700,
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Grey100,
        onTertiaryContainer = Grey800,
        background = Grey100,
        onBackground = Grey900,
        surface = Grey100,
        onSurface = Grey900,
        surfaceVariant = Grey100,
        onSurfaceVariant = Grey600,
        inverseSurface = Grey900,
        inverseOnSurface = Grey50,
        outline = Grey400,
        outlineVariant = Grey200,
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFFFFFF),
        surfaceContainer = Grey100,
        surfaceContainerHigh = Grey200,
        surfaceContainerHighest = Grey300,
    )

// 다크 뉴트럴도 같은 원칙 — 캔버스가 가장 어둡고 카드가 한 단계 밝다.
private val DarkGrey900 = Color(0xFF17171C)
private val DarkGrey800 = Color(0xFF1F1F26)
private val DarkGrey700 = Color(0xFF262630)
private val DarkGrey600 = Color(0xFF2E2E3A)
private val DarkGrey500 = Color(0xFF3A3A47)
private val DarkGrey400 = Color(0xFF6B7280)
private val DarkGrey200 = Color(0xFF9AA0A9)
private val DarkGrey50 = Color(0xFFEDEDF0)

val DarkColorScheme =
    darkColorScheme(
        primary = DarkBrandPrimary,
        onPrimary = Color(0xFF00391B),
        // 라이트와 같은 원칙 — 다크에서도 초록은 이 톤 하나만 쓴다.
        primaryContainer = DarkBrandPrimary,
        onPrimaryContainer = Color(0xFF00391B),
        inversePrimary = BrandPrimary,
        secondary = DarkBrandPrimary,
        onSecondary = Color(0xFF00391B),
        secondaryContainer = DarkBrandPrimary,
        onSecondaryContainer = Color(0xFF00391B),
        tertiary = DarkGrey200,
        onTertiary = DarkGrey900,
        tertiaryContainer = DarkGrey600,
        onTertiaryContainer = DarkGrey50,
        background = DarkGrey900,
        onBackground = DarkGrey50,
        surface = DarkGrey900,
        onSurface = DarkGrey50,
        surfaceVariant = DarkGrey700,
        onSurfaceVariant = DarkGrey200,
        inverseSurface = DarkGrey50,
        inverseOnSurface = DarkGrey900,
        outline = DarkGrey400,
        outlineVariant = DarkGrey600,
        surfaceContainerLowest = DarkGrey900,
        surfaceContainerLow = DarkGrey800,
        surfaceContainer = DarkGrey700,
        surfaceContainerHigh = DarkGrey600,
        surfaceContainerHighest = DarkGrey500,
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
