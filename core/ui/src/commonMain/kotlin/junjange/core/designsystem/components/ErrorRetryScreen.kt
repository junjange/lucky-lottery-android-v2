package junjange.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import junjange.core.designsystem.theme.LottoShapeTokens
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.ui.resources.Res
import junjange.core.ui.resources.error_retry_button
import org.jetbrains.compose.resources.stringResource

/**
 * 불러오기에 실패했을 때의 화면.
 *
 * 예전에는 글자 크기를 18sp/16sp로 직접 박고 여백도 5dp·30dp처럼 스케일 밖 값을 썼다.
 * 타이포는 테마 슬롯에서, 여백은 [LottoSpacing]에서 가져와 다른 화면과 리듬을 맞춘다.
 */
@Composable
fun ErrorRetryScreen(
    title: String,
    description: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = LottoSpacing.screenHorizontal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(LottoSpacing.sm))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(LottoSpacing.xl))

        // 폭을 못 박지 않는다. 글자가 길어지거나 시스템 글꼴이 커지면 120dp 안에서 잘렸다.
        Button(
            onClick = onRetry,
            shape = LottoShapeTokens.button,
        ) {
            Text(
                text = stringResource(Res.string.error_retry_button),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
