package junjange.core.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LottoSwitchBar(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    @StringRes descriptionTextRes: Int,
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
                text = stringResource(id = textRes),
                style = LottoTheme.typography.body1,
                color = LottoTheme.colors.lottoBlack,
            )
            Text(
                text = stringResource(id = descriptionTextRes),
                style = LottoTheme.typography.caption2,
                color = LottoTheme.colors.gray600,
            )
        }

        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Switch(
                checked = isSwitchedOn,
                onCheckedChange = { if (it) onSwitchOn() else onSwitchOff() },
                colors = SwitchDefaults.colors(checkedTrackColor = LottoTheme.colors.lottoGreen),
            )
        }
    }
}
