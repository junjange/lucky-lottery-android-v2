package com.junjange.presentation.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.junjange.presentation.R
import com.junjange.presentation.theme.LottoTheme

@Composable
fun LotteryDeleteDialog(
    onDismiss: () -> Unit,
    okClick: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        LotteryDeleteDialogContent(okClick)
    }
}

@Composable
private fun LotteryDeleteDialogContent(okClick: () -> Unit) {
    Surface(
        modifier =
            Modifier
                .width(312.dp)
                .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 15.dp,
        color = LottoTheme.colors.white,
    ) {
        Column {
            Text(
                text = stringResource(R.string.dialog_delete_title),
                modifier = Modifier.padding(16.dp),
                style = LottoTheme.typography.headline3,
                color = LottoTheme.colors.lottoBlack,
            )
            Text(
                text = stringResource(R.string.dialog_delete_message),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = LottoTheme.typography.body3,
                color = LottoTheme.colors.gray700,
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp, end = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier =
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(18.dp)
                            .clickable { okClick() },
                    text = stringResource(id = R.string.button_done),
                    style = LottoTheme.typography.body2,
                    color = LottoTheme.colors.lottoBlue,
                )
            }
        }
    }
}
