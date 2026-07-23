package junjange.core.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * OS 위임형 디자인 시스템: M3 기본 Button을 사용한다 (primary 자동 적용).
 * backgroundColor는 하위 호환을 위해 남겨둔 무시 파라미터.
 */
@Composable
fun LottoRoundedCornerButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    backgroundColor: Color = Color.Unspecified,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = isEnabled,
    ) {
        Text(text = buttonText)
    }
}
