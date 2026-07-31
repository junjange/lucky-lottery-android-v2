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
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import junjange.core.designsystem.components.ErrorRetryScreen
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.ui.component.LottoContent
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
    // 큰 제목이 스크롤에 따라 접힌다. iOS 대형 타이틀과 같은 동작이라 셸이 SwiftUI든 아니든
    // 어색하지 않고, 접히고 나면 목록에 세로 공간을 돌려준다.
    val topBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
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
        modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(Res.string.home_top_bar_title)) },
                scrollBehavior = topBarScrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            // 색은 테마의 primaryContainer가 이미 브랜드 그린이라 따로 지정하지 않는다.
            FloatingActionButton(onClick = navigateToQRScanner) {
                Icon(
                    painter = painterResource(Res.drawable.ic_qr_code),
                    contentDescription = stringResource(Res.string.qr_scan_description),
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

            Column(Modifier.fillMaxSize()) {
                // 배너는 스크롤 밖에 둔다. 예전에는 목록과 같이 올라가서 랜덤 번호 생성 화면의
                // 고정 배너와 동작이 달랐다.
                PlatformAdBanner(modifier = Modifier.fillMaxWidth())

                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(modifier = Modifier.height(LottoSpacing.base))
                    LottoContent(
                    lotteryNumbers = state.lotteryNumbers,
                    pensionLotteryHome = state.pensionLotteryHome,
                    changeLottery = { offset ->
                        viewModel.event(Event.ChangeLottery(offset = offset))
                    },
                    changePensionLottery = { offset ->
                        viewModel.event(Event.ChangePensionLottery(offset = offset))
                    },
                    canGoPreviousLottery = state.canGoPreviousLottery,
                    canGoNextLottery = state.canGoNextLottery,
                        canGoPreviousPension = state.canGoPreviousPension,
                        canGoNextPension = state.canGoNextPension,
                    )
                    // FAB와 하단 탭에 마지막 카드가 가리지 않을 만큼의 여유.
                    Spacer(modifier = Modifier.height(LottoSpacing.xxxl * 2))
                }
            }
        }
    }
}
