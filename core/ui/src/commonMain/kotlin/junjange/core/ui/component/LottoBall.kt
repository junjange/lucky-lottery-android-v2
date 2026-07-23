package junjange.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.domain.model.LottoType

@Composable
fun LottoBall(
    lottoType: LottoType,
    lottoColor: Color,
    lottoTitle: String,
) {
    Surface(
        modifier = Modifier.size(30.dp),
        border =
            if (lottoType == LottoType.LOTTO720) {
                BorderStroke(
                    width = 4.dp,
                    color = lottoColor,
                )
            } else {
                null
            },
        shape = CircleShape,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = if (lottoType == LottoType.LOTTO720) LottoTheme.colors.white else lottoColor),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = lottoTitle,
                textAlign = TextAlign.Center,
                style =
                    LottoTheme.typography.body3.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (lottoType == LottoType.LOTTO720) LottoTheme.colors.black else LottoTheme.colors.white,
                    ),
            )
        }
    }
}
