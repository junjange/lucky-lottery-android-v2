package junjange.core.ui.component

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
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.lotteryColors
import junjange.core.ui.R

@Composable
fun PensionLotteryNumberEntry(
    onSubmit: (List<String>) -> Unit,
    onInvalidGroup: () -> Unit,
) {
    val focusRequesters = List(7) { FocusRequester() }
    val pensionLottery = remember { mutableStateListOf("", "", "", "", "", "", "") }
    val enabled = pensionLottery.all { it.isNotBlank() }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            stringResource(R.string.enter_pension_lottery_number),
            style = LottoTheme.typography.headline3,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    12.dp,
                    Alignment.CenterHorizontally,
                ),
        ) {
            for (i in pensionLottery.indices) {
                if (i == 1) {
                    Text(
                        text = stringResource(id = R.string.group_title),
                        style = LottoTheme.typography.headline3,
                    )
                }

                PensionLotteryBallTextField(
                    value = pensionLottery[i],
                    onValueChange = { newValue ->
                        if (newValue.isBlank() || (newValue.isDigitsOnly() && newValue.toIntOrNull() in 0..9)) {
                            pensionLottery[i] = newValue
                        }
                        if (pensionLottery[i].length == 1 && i < 6) {
                            focusRequesters.getOrNull(i + 1)?.requestFocus()
                        }
                    },
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                if (i < 6) {
                                    focusRequesters.getOrNull(i + 1)?.requestFocus()
                                }
                            },
                        ),
                    color = lotteryColors[i],
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
                    if (pensionLottery.first().toInt() in 1..5) {
                        onSubmit(pensionLottery.toList())
                    } else {
                        onInvalidGroup()
                    }
                }
            },
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}
