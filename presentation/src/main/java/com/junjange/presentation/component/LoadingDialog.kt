package com.junjange.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.junjange.presentation.ui.theme.LottoTheme

@Composable
fun LoadingDialog(modifier: Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = Color.Gray,
            strokeWidth = 2.dp,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun LoadingDialogPreview() {
    LottoTheme {
        LoadingDialog(modifier = Modifier)
    }
}
