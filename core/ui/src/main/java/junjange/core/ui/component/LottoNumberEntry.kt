package junjange.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.R

private class LottoEntryGame {
    val numbers = mutableStateListOf("", "", "", "", "", "")
    val focusRequesters = List(6) { FocusRequester() }
}

@Composable
fun LottoNumberEntry(
    onSubmit: (List<List<String>>) -> Unit,
    onDuplicateLottery: () -> Unit,
) {
    val games = remember { mutableStateListOf(LottoEntryGame()) }
    val enabled = games.all { game -> game.numbers.all { it.isNotBlank() } }
    var previousGameCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(games.size) {
        if (games.size > previousGameCount) {
            games.last().focusRequesters[0].requestFocus()
        }
        previousGameCount = games.size
    }

    Column(
        modifier =
            Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
    ) {
        Text(stringResource(R.string.enter_lotto_numbers), style = LottoTheme.typography.headline3)

        Spacer(modifier = Modifier.height(20.dp))

        games.forEachIndexed { gameIndex, game ->
            key(game) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally,
                        ),
                ) {
                    for (i in game.numbers.indices) {
                        LottoBallTextField(
                            value = game.numbers[i],
                            onValueChange = { newValue ->
                                if (newValue.isBlank() || (newValue.isDigitsOnly() && newValue.toIntOrNull() in 1..45)) {
                                    game.numbers[i] = newValue
                                }
                                if ((game.numbers[i].toIntOrNull() in 5..9 || game.numbers[i].length == 2) && i < 5) {
                                    game.focusRequesters.getOrNull(i + 1)?.requestFocus()
                                }
                            },
                            keyboardActions =
                                KeyboardActions(
                                    onDone = {
                                        if (i < 5) {
                                            game.focusRequesters.getOrNull(i + 1)?.requestFocus()
                                        }
                                    },
                                ),
                            modifier = Modifier.focusRequester(game.focusRequesters[i]),
                        )
                    }

                    Box(modifier = Modifier.size(24.dp)) {
                        if (games.size > 1) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.remove_number),
                                tint = LottoTheme.colors.gray400,
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .clickable { games.removeAt(gameIndex) },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp))
                    .clickable { games.add(LottoEntryGame()) }
                    .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = LottoTheme.colors.green,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.add_number),
                style = LottoTheme.typography.body3,
                color = LottoTheme.colors.green,
            )
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
                    if (games.all { it.numbers.toSet().size == 6 }) {
                        onSubmit(games.map { it.numbers.toList() })
                    } else {
                        onDuplicateLottery()
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}
