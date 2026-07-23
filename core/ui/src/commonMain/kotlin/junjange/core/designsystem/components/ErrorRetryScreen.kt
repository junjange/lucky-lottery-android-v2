package junjange.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import junjange.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource
import junjange.core.designsystem.theme.Green
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.White

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
                .background(LottoTheme.colors.white),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = LottoTheme.colors.black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            color = LottoTheme.colors.gray500,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onRetry,
            modifier =
                Modifier
                    .width(120.dp),
            shape = RoundedCornerShape(8.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Green,
                ),
        ) {
            Text(
                text = stringResource(Res.string.error_retry_button),
                style =
                    LottoTheme.typography.body3.copy(
                        color = White,
                        fontWeight = FontWeight.Bold,
                    ),
            )
        }
    }
}

