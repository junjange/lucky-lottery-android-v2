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
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.junjange.presentation.theme.LottoTheme

@Composable
fun LottoNumberEntry(onSaveClicked: (List<String>) -> Unit) {
    val focusRequesters = List(6) { FocusRequester() }
    val lottery = remember { mutableStateListOf("", "", "", "", "", "") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("로또 번호를 직접 입력해봐요", style = LottoTheme.typography.headline3)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterHorizontally,
                ),
        ) {
            for (i in 0 until 6) {
                LottoBallTextField(
                    value = lottery[i],
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || (newValue.isDigitsOnly() && newValue.toIntOrNull() in 1..45)) {
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
                if (lottery.filter { it.isNotBlank() }.size == 6) {
                    if (lottery.toSet().size == 6) {
                        onSaveClicked(lottery.toList())
                        Toast.makeText(context, "로또 번호가 제출되었습니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "로또 번호가 중복되었습니다!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "6개의 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("번호 제출")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
