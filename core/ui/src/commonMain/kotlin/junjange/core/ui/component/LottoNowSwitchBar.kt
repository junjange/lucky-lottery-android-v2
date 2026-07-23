package junjange.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LottoSwitchBar(
    modifier: Modifier = Modifier,
    textRes: StringResource,
    descriptionTextRes: StringResource,
    isSwitchedOn: Boolean,
    onSwitchOn: () -> Unit,
    onSwitchOff: () -> Unit,
) {
    Row(
        modifier =
            modifier.then(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = stringResource(textRes),
                style = LottoTheme.typography.body1,
                color = LottoTheme.colors.lottoBlack,
            )
            Text(
                text = stringResource(descriptionTextRes),
                style = LottoTheme.typography.caption2,
                color = LottoTheme.colors.gray600,
            )
        }

        Switch(
            checked = isSwitchedOn,
            onCheckedChange = { if (it) onSwitchOn() else onSwitchOff() },
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = LottoTheme.colors.white,
                    checkedTrackColor = LottoTheme.colors.green,
                    uncheckedThumbColor = LottoTheme.colors.gray500,
                    uncheckedTrackColor = LottoTheme.colors.gray300,
                ),
        )
    }
}
