package com.junjange.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.junjange.presentation.theme.LottoTheme

@Composable
fun PensionLotteryBallTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier.size(30.dp),
        border =
            BorderStroke(
                width = 4.dp,
                color = color,
            ),
        shape = CircleShape,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = LottoTheme.colors.white),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle =
                    LottoTheme.typography.body3.copy(
                        fontWeight = FontWeight.Bold,
                        color = LottoTheme.colors.black,
                        textAlign = TextAlign.Center,
                    ),
                keyboardActions = keyboardActions,
                keyboardOptions =
                    KeyboardOptions.Default.copy(
                        keyboardType =
                            KeyboardType.Number,
                    ),
                modifier =
                    modifier.align(Alignment.Center),
            )
        }
    }
}
