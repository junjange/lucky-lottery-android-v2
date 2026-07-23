package junjange.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoTheme

@Composable
fun LottoTextBar(
    modifier: Modifier = Modifier,
    text: String,
    subtext: String,
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
        Text(
            text = text,
            style = LottoTheme.typography.body1,
            color = LottoTheme.colors.lottoBlack,
        )
        Text(
            text = subtext,
            style = LottoTheme.typography.caption1,
            color = LottoTheme.colors.lottoBlack,
        )
    }
}
