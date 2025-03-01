package com.junjange.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junjange.presentation.R
import com.junjange.presentation.component.AdmobBanner
import com.junjange.presentation.component.LottoContent
import com.junjange.presentation.component.LottoHomeTopBar
import com.junjange.presentation.ui.theme.LottoTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToQRScanner: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottoHomeTopBar()
            AdmobBanner(modifier = Modifier.fillMaxWidth())
            LottoContent(
                lotteryNumbers = state.lotteryNumbers,
                pensionLotteryHome = state.pensionLotteryHome,
                changeLottery = { offset ->
                    viewModel.event(HomeContract.Event.ChangeLottery(offset = offset))
                },
                changePensionLottery = { offset ->
                    viewModel.event(HomeContract.Event.ChangePensionLottery(offset = offset))
                },
            )
            Spacer(modifier = Modifier.height(50.dp))
        }

        FloatingActionButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 15.dp, end = 15.dp),
            containerColor = LottoTheme.colors.green,
            onClick = navigateToQRScanner,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_qr_code),
                contentDescription = null,
                tint = LottoTheme.colors.white,
            )
        }
    }
}
