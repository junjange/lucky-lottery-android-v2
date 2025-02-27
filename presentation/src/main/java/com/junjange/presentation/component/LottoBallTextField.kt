package com.junjange.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.junjange.presentation.ui.theme.LottoTheme
import com.junjange.presentation.ui.theme.toLotteryColor

@Composable
fun LottoBallTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
) {
    val color = value.toIntOrNull().toLotteryColor()

    Surface(
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = color),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle =
                    LottoTheme.typography.body3.copy(
                        fontWeight = FontWeight.Bold,
                        color = LottoTheme.colors.white,
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
