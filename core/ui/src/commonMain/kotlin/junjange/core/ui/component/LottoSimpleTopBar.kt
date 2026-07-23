package junjange.core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LottoSimpleTopBar(
    modifier: Modifier = Modifier,
    backIconRes: DrawableResource? = null,
    onBack: (() -> Unit)? = null,
    titleRes: StringResource,
    actionIconRes: DrawableResource? = null,
    onAction: (() -> Unit)? = null,
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
            backIconRes?.let {
                LottoIconButton(
                    iconRes = backIconRes,
                    tint = LottoTheme.colors.lottoBlack,
                    size = 24.dp,
                    onClick = onBack ?: {},
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Text(
                text = stringResource(titleRes),
                style = LottoTheme.typography.headline2,
                color = LottoTheme.colors.lottoBlack,
            )
            Spacer(modifier = Modifier.weight(1f))
            actionIconRes?.let {
                LottoIconButton(
                    iconRes = it,
                    tint = LottoTheme.colors.lottoBlack,
                    size = 24.dp,
                    onClick = onAction ?: {},
                )
            }
        }
    }
}

private val SIMPLE_TOP_BAR_HEIGHT = 64.dp
