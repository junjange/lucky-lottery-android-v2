package junjange.core.designsystem.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import junjange.core.designsystem.theme.LottoShapeTokens
import junjange.core.designsystem.theme.LottoSpacing

/** 확인 버튼 높이. 화면 폭을 다 쓰는 주 동작이라 하단 고정 버튼과 같게 둔다. */
private val ConfirmButtonHeight = 56.dp

/**
 * 확인·취소 두 갈래 대화상자.
 *
 * 삭제처럼 되돌릴 수 없는 동작에도 확인 버튼 색을 바꾸지 않는다. 무엇이 일어나는지는 제목과
 * 본문이 말하고, 앱 안의 확인 버튼은 한 가지 색으로 두는 편이 덜 낯설다.
 *
 * 모서리와 타이포는 토큰에서 가져온다. 예전에는 10dp·8dp 모서리와 레거시 타이포를 직접 써서
 * 같은 화면에 뜨는 시트·카드와 모서리가 어긋났다.
 */
@Composable
fun LottoTwoButtonDialog(
    title: String,
    content: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = LottoShapeTokens.card,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LottoSpacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(LottoSpacing.xl))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(LottoSpacing.sm))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = content,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(LottoSpacing.xl))

                Button(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(ConfirmButtonHeight),
                    onClick = onConfirm,
                    shape = LottoShapeTokens.button,
                ) {
                    Text(text = confirmText, style = MaterialTheme.typography.titleMedium)
                }

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancel,
                ) {
                    Text(text = cancelText, style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(LottoSpacing.base))
            }
        }
    }
}
