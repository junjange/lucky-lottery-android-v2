package junjange.core.designsystem.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import junjange.core.designsystem.theme.LottoTheme

/**
 * 확인·취소 두 갈래 대화상자.
 *
 * 삭제처럼 되돌릴 수 없는 동작에도 확인 버튼 색을 바꾸지 않는다. 무엇이 일어나는지는 제목과
 * 본문이 말하고, 앱 안의 확인 버튼은 한 가지 색으로 두는 편이 덜 낯설다.
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
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp),
                    textAlign = TextAlign.Center,
                    style =
                        LottoTheme.typography.headline2.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.ExtraBold,
                        ),
                )

                Text(
                    text = content,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    style =
                        LottoTheme.typography.body3.copy(
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                )

                Button(
                    onClick = onConfirm,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp)
                            .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = confirmText,
                        style =
                            LottoTheme.typography.body3.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                    )
                }

                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = cancelText,
                        style =
                            LottoTheme.typography.body3.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                            ),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
