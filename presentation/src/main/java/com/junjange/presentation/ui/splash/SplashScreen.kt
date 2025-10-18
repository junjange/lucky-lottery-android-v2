package com.junjange.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.junjange.presentation.R
import junjange.core.designsystem.theme.LottoTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navigateToMain: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SplashContract.Effect.AlreadyLoggedIn -> navigateToMain()
                SplashContract.Effect.RequireLoginIn -> navigateToLogin()
            }
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(LottoTheme.colors.lottoWhite)
                .padding(top = (screenHeight / 7 * 3).dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = null,
                modifier =
                    Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = "일상속에서 행운을 찾다",
                style = LottoTheme.typography.headline3,
                color = LottoTheme.colors.lottoBlack,
            )
        }
    }
}
