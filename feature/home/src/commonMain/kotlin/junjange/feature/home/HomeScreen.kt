package junjange.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import junjange.core.designsystem.components.ErrorRetryScreen
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.component.LottoContent
import junjange.core.ui.component.LottoHomeTopBar
import junjange.core.ui.platform.PlatformAdBanner
import junjange.feature.home.HomeContract.*
import junjange.feature.home.resources.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToQRScanner: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val refreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

    val lottoNotFound = stringResource(Res.string.lotto_number_not_found_message)
    val pensionNotFound = stringResource(Res.string.pension_number_not_found_message)

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.ShowMessage -> {
                    val message = when (effect.message) {
                        HomeMessage.LOTTO_NUMBER_NOT_FOUND -> lottoNotFound
                        HomeMessage.PENSION_NUMBER_NOT_FOUND -> pensionNotFound
                    }
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToQRScanner,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_qr_code),
                    contentDescription = null,
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
                    state = refreshState,
                )
            },
        ) {
            if (state.isError) {
                ErrorRetryScreen(
                    title = stringResource(Res.string.error_network_title),
                    description = stringResource(Res.string.error_network_description),
                    onRetry = {
                        viewModel.event(Event.Refresh)
                    },
                )
                return@PullToRefreshBox
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LottoHomeTopBar()
                PlatformAdBanner(modifier = Modifier.fillMaxWidth())
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
