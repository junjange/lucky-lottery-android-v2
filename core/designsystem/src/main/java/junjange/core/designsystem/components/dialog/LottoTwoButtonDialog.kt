package junjange.core.designsystem.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import junjange.core.designsystem.theme.Gray800
import junjange.core.designsystem.theme.Gray900
import junjange.core.designsystem.theme.Green
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.White

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
            color = Color.White,
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
                            color = Gray900,
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
                            Gray800,
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
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Green,
                        ),
                ) {
                    Text(
                        text = confirmText,
                        style =
                            LottoTheme.typography.body3.copy(
                                color = White,
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
                                color = Gray800,
                                fontWeight = FontWeight.Bold,
                            ),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
