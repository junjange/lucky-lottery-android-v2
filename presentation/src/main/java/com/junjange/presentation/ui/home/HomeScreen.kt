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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junjange.presentation.R
import com.junjange.presentation.component.AdmobBanner
import com.junjange.presentation.component.LoadingDialog
import com.junjange.presentation.component.LottoContent
import com.junjange.presentation.component.LottoHomeTopBar
import com.junjange.presentation.theme.LottoTheme
import com.junjange.presentation.util.showToast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToQRScanner: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeContract.Effect.ShowMessage -> {
                    val message =
                        when (effect.message) {
                            HomeMessage.LOTTO_NUMBER_NOT_FOUND -> R.string.lotto_number_not_found_message
                            HomeMessage.PENSION_NUMBER_NOT_FOUND -> R.string.pension_number_not_found_message
                        }
                    context.showToast(message)
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = LottoTheme.colors.green,
                onClick = navigateToQRScanner,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_qr_code),
                    contentDescription = null,
                    tint = LottoTheme.colors.white,
                )
            }
        },
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingDialog(modifier = Modifier.fillMaxSize())
            return@Scaffold
        }

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
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
        }
    }
}
