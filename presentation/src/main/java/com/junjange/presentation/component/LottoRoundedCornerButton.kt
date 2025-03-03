package com.junjange.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.junjange.presentation.theme.LottoTheme

@Composable
inline fun LottoRoundedCornerButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    backgroundColor: Color,
    isEnabled: Boolean = true,
    crossinline onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .background(if (isEnabled) backgroundColor else LottoTheme.colors.gray400)
                .clickable(enabled = isEnabled) {
                    onClick()
                },
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = buttonText,
            style = LottoTheme.typography.body3,
            color = LottoTheme.colors.white,
        )
    }
}
