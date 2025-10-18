package com.junjange.presentation.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junjange.presentation.R
import com.junjange.presentation.ui.home.HomeContract.*
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.component.AdmobBanner
import junjange.core.ui.component.LottoContent
import junjange.core.ui.component.LottoHomeTopBar
import junjange.core.ui.util.showToast
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToQRScanner: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val refreshState = rememberPullToRefreshState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.ShowMessage -> {
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
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = {
                viewModel.event(Event.Refresh)
            },
            state = refreshState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = state.isLoading,
                    containerColor = LottoTheme.colors.white,
                    color = LottoTheme.colors.black,
                    state = refreshState,
                )
            },
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
                        viewModel.event(Event.ChangeLottery(offset = offset))
                    },
                    changePensionLottery = { offset ->
                        viewModel.event(Event.ChangePensionLottery(offset = offset))
                    },
                )
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
