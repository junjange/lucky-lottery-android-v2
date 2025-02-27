package com.junjange.presentation.component

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.junjange.presentation.R
import com.junjange.presentation.ui.theme.LottoTheme
import com.junjange.presentation.ui.theme.lotteryColors

@Composable
fun PensionLotteryNumberEntry(onSaveClicked: (List<String>) -> Unit) {
    val focusRequesters = List(7) { FocusRequester() }
    val pensionLottery = remember { mutableStateListOf("", "", "", "", "", "", "") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("연급복권 번호를 직접 입력해봐요", style = LottoTheme.typography.headline3)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    12.dp,
                    Alignment.CenterHorizontally,
                ),
        ) {
            for (i in 0 until 7) {
                if (i == 1) {
                    Text(
                        text = stringResource(id = R.string.group_title),
                        style = LottoTheme.typography.headline3,
                    )
                }

                PensionLotteryBallTextField(
                    value = pensionLottery[i],
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || (newValue.isDigitsOnly() && newValue.toIntOrNull() in 0..9)) {
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
                    modifier =
                        Modifier.run { focusRequester(focusRequesters[i]) },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                if (pensionLottery.filter { it.isNotBlank() }.size == 7) {
                    if (pensionLottery.first().toInt() in 1..5) {
                        onSaveClicked(pensionLottery.toList())
                        Toast.makeText(context, "연금복권 번호가 제출되었습니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast
                            .makeText(context, "조는 1부터 5 사이에 숫자중 하나를 입력해주세요", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "1개의 조와 6개의 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("번호 추가")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
