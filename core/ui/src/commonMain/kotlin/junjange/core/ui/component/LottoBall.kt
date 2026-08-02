package junjange.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import junjange.core.domain.model.LottoType

/** 볼 지름 기본값. 목록·입력 화면처럼 볼이 여러 줄 들어가는 곳의 크기. */
val LottoBallDefaultSize: Dp = 30.dp

/**
 * 볼 여섯 개 앞에 조 번호 칩이 붙는 연금복권용 크기.
 * 칩과 간격을 뺀 한 줄에 여섯 개가 들어가는 상한이 32dp다.
 */
val LottoBallMediumSize: Dp = 32.dp

/**
 * 당첨번호를 주인공으로 보여주는 자리에서 쓰는 크기.
 * 좌우 여백을 뺀 한 줄에 볼 여섯 개가 들어가는 상한이 36dp라 더 키우지 않는다.
 */
val LottoBallLargeSize: Dp = 36.dp

/**
 * 아직 정해지지 않은 번호 자리.
 *
 * 옅은 회색 배경에 흰 글자를 얹으면 사실상 보이지 않는다. 값이 아직 없다는 것은 색을 빼서 알리고,
 * 글자는 배경과 충분히 대비되는 뉴트럴로 둔다. 실제 볼 색을 쓰면 이미 추첨된 것처럼 읽혀서 안 된다.
 */
@Composable
fun LottoBallPlaceholder(
    lottoTitle: String,
    size: Dp = LottoBallDefaultSize,
) {
    Surface(
        modifier = Modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = lottoTitle,
                textAlign = TextAlign.Center,
                style =
                    if (size >= LottoBallLargeSize) {
                        MaterialTheme.typography.titleMedium
                    } else {
                        MaterialTheme.typography.titleSmall
                    },
            )
        }
    }
}

@Composable
fun LottoBall(
    lottoType: LottoType,
    lottoColor: Color,
    lottoTitle: String,
    size: Dp = LottoBallDefaultSize,
) {
    val isPension = lottoType == LottoType.LOTTO720

    Surface(
        modifier = Modifier.size(size),
        // 연금복권은 실물이 흰 볼에 색 테두리라 채우지 않고 테두리로만 색을 준다.
        border = if (isPension) BorderStroke(width = 4.dp, color = lottoColor) else null,
        shape = CircleShape,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = if (isPension) Color.White else lottoColor),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = lottoTitle,
                textAlign = TextAlign.Center,
                style =
                    if (size >= LottoBallLargeSize) {
                        MaterialTheme.typography.titleMedium
                    } else {
                        MaterialTheme.typography.titleSmall
                    }.copy(color = if (isPension) Color.Black else Color.White),
            )
        }
    }
}
