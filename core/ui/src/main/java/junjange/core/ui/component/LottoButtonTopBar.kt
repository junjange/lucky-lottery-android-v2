package junjange.core.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.R

@Composable
fun LottoButtonTopBar(
    modifier: Modifier = Modifier,
    @DrawableRes backIconRes: Int = R.drawable.ic_back,
    onBack: () -> Unit,
    @StringRes titleRes: Int,
    @StringRes buttonTextRes: Int,
    onClickButton: () -> Unit,
    isEnabled: Boolean = true,
) {
    Surface(
        modifier =
            modifier.then(
                Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .height(SIMPLE_TOP_BAR_HEIGHT)
                    .fillMaxWidth(),
            ),
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LottoIconButton(
                iconRes = backIconRes,
                tint = LottoTheme.colors.lottoBlack,
                size = 24.dp,
                onClick = onBack,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = titleRes),
                style = LottoTheme.typography.headline2,
                color = LottoTheme.colors.lottoBlack,
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onClickButton,
                enabled = isEnabled,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = stringResource(id = buttonTextRes),
                    style = LottoTheme.typography.body3,
                )
            }
        }
    }
}

private val SIMPLE_TOP_BAR_HEIGHT = 64.dp
