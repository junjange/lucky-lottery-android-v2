package com.junjange.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.junjange.presentation.theme.LottoTheme

@Composable
fun LottoSimpleTopBar(
    modifier: Modifier = Modifier,
    @DrawableRes backIconRes: Int? = null,
    onBack: (() -> Unit)? = null,
    @StringRes titleRes: Int,
    @DrawableRes actionIconRes: Int? = null,
    onAction: (() -> Unit)? = null,
) {
    Surface(
        modifier =
            modifier.then(
                Modifier
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
                text = stringResource(id = titleRes),
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
