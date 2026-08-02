package junjange.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * 모서리 토큰.
 *
 * M3 컴포넌트는 이 슬롯을 읽으므로, 여기만 바꾸면 카드·다이얼로그·메뉴의 곡률이 함께 따라온다.
 * 8dp 미만은 각져 보이고 20dp를 넘으면 알약처럼 보이므로 10~18dp를 주로 쓴다.
 */
val LottoShapes =
    Shapes(
        extraSmall = RoundedCornerShape(6.dp),
        small = RoundedCornerShape(10.dp),
        medium = RoundedCornerShape(14.dp),
        large = RoundedCornerShape(18.dp),
        extraLarge = RoundedCornerShape(24.dp),
    )

/**
 * M3가 곡률을 하드코딩해서 `Shapes`로는 못 바꾸는 자리에 쓰는 토큰.
 * Button 기본값은 알약 모양이라, 사각에 가까운 CTA를 쓰려면 `shape`를 직접 넘겨야 한다.
 */
object LottoShapeTokens {
    val button = RoundedCornerShape(14.dp)
    val card = RoundedCornerShape(16.dp)
    val ball = RoundedCornerShape(percent = 50)
}
