package com.junjange.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.junjange.presentation.R
import com.junjange.presentation.theme.LottoTheme

@Composable
fun LottoNumberEntry(
    onSubmit: (List<String>) -> Unit,
    onDuplicateLottery: () -> Unit,
) {
    val focusRequesters = List(6) { FocusRequester() }
    val lottery = remember { mutableStateListOf("", "", "", "", "", "") }
    val enabled = lottery.all { it.isNotBlank() }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(stringResource(R.string.enter_lotto_numbers), style = LottoTheme.typography.headline3)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterHorizontally,
                ),
        ) {
            for (i in lottery.indices) {
                LottoBallTextField(
                    value = lottery[i],
                    onValueChange = { newValue ->
                        if (newValue.isBlank() || (newValue.isDigitsOnly() && newValue.toIntOrNull() in 1..45)) {
                            lottery[i] = newValue
                        }
                        if ((lottery[i].toIntOrNull() in 5..9 || lottery[i].length == 2) && i < 5) {
                            focusRequesters.getOrNull(i + 1)?.requestFocus()
                        }
                    },
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                if (i < 5) {
                                    focusRequesters.getOrNull(i + 1)?.requestFocus()
                                }
                            },
                        ),
                    modifier = Modifier.focusRequester(focusRequesters[i]),
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LottoRoundedCornerButton(
            modifier =
                Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .height(40.dp)
                    .fillMaxWidth(),
            buttonText = stringResource(R.string.create_title),
            backgroundColor = LottoTheme.colors.green,
            isEnabled = enabled,
            onClick = {
                if (enabled) {
                    if (lottery.toSet().size == 6) {
                        onSubmit(lottery.toList())
                    } else {
                        onDuplicateLottery()
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}
