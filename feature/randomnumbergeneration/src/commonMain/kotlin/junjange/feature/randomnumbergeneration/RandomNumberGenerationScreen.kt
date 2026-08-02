package junjange.feature.randomnumbergeneration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import junjange.core.designsystem.theme.LottoShapeTokens
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.designsystem.theme.toLotteryColor
import junjange.core.domain.model.LottoType
import junjange.core.ui.component.LottoBall
import junjange.core.ui.component.LottoBallLargeSize
import junjange.core.ui.component.LottoBallPlaceholder
import junjange.core.ui.component.LottoGroupChip
import junjange.core.ui.component.LottoPensionBalls
import junjange.core.ui.platform.PlatformAdBanner
import junjange.feature.randomnumbergeneration.RandomNumberGenerationContract.*
import junjange.feature.randomnumbergeneration.resources.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomNumberGenerationScreen(
    viewModel: RandomNumberGenerationViewModel,
    navigateToMain: (initialPage: String) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val lottoSubmittedMsg = stringResource(Res.string.lotto_number_submitted)
    val pensionSubmittedMsg = stringResource(Res.string.pension_lottery_number_submitted)
    val checkNumberLabel = stringResource(Res.string.action_check_number)

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.Finish -> onBack()
                is Effect.ShowMessage -> {
                    val (message, initialPage) =
                        when (effect.message) {
                            RandomNumberGenerationMessage.LOTTERY_NUMBER_SAVED ->
                                Pair(lottoSubmittedMsg, "0")
                            RandomNumberGenerationMessage.PENSION_LOTTERY_SAVED ->
                                Pair(pensionSubmittedMsg, "1")
                        }
                    val result =
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = checkNumberLabel,
                            duration = SnackbarDuration.Short,
                        )
                    when (result) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            navigateToMain(initialPage)
                        }
                    }
                }
            }
        }
    }

    val title =
        if (state.isLotto645) {
            stringResource(Res.string.lotto_645_random_title)
        } else {
            stringResource(Res.string.lotto_720_title)
        }
    val hasNumbers = state.lotteryRandomNumbers != null || state.pensionLotteryRandom != null

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(text = title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.event(Event.Back) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_chevron_left),
                            contentDescription = stringResource(Res.string.action_back),
                        )
                    }
                },
                // 번호 담기 화면과 같은 자리 나눔이다. 반복하는 동작은 하단 고정,
                // 마지막 동작(저장)은 상단 오른쪽에 둔다.
                actions = {
                    TextButton(
                        onClick = {
                            if (state.isLotto645) {
                                viewModel.event(Event.SaveLottery)
                            } else {
                                viewModel.event(Event.SavePensionLottery)
                            }
                        },
                        enabled = state.saveIsEnabled,
                    ) {
                        Text(
                            text = stringResource(Res.string.save_title),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                },
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Button(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                            .padding(
                                horizontal = LottoSpacing.screenHorizontal,
                                vertical = LottoSpacing.md,
                            ).height(56.dp),
                    onClick = {
                        if (state.isLotto645) {
                            viewModel.event(Event.GenerateRandomLottery)
                        } else {
                            viewModel.event(Event.GenerateRandomPensionLottery)
                        }
                    },
                    shape = LottoShapeTokens.button,
                ) {
                    Text(
                        // 다시 누르면 지금 번호가 바뀐다는 것을 라벨로 알린다.
                        text =
                            stringResource(
                                if (hasNumbers) Res.string.random_regenerate else Res.string.random_generate,
                            ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = MaterialTheme.colorScheme.inversePrimary,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            // 배너를 형제로 두어 자리를 차지하게 한다. 예전에는 본문과 같은 Box에 겹쳐 있어서
            // 아무도 배너 자리를 비워두지 않았고, 내용이 길어지면 그 위를 덮었다.
            PlatformAdBanner(modifier = Modifier.fillMaxWidth())

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RandomNumberGenerationContent(state = state)
            }
        }
    }
}

/**
 * 만들어진 번호를 보여주는 본문.
 *
 * 복권 이름은 상단 바가 이미 달고 있어서 본문에서 뺐다. 전체 화면 푸시라 상단 바가 늘 보이는데
 * 같은 이름을 두 번 읽게 할 이유가 없다.
 */
@Composable
fun RandomNumberGenerationContent(state: State) {
    Image(
        modifier = Modifier.size(140.dp),
        painter = painterResource(Res.drawable.ic_random_poster),
        contentDescription = null,
    )

    Spacer(modifier = Modifier.height(LottoSpacing.xxl))

    // 이 화면의 주인공이므로 볼을 홈보다 크게 둔다.
    Row(
        horizontalArrangement = Arrangement.spacedBy(LottoSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (state.isLotto645) {
            val numbers =
                state.lotteryRandomNumbers?.let {
                    listOf(it.firstNum, it.secondNum, it.thirdNum, it.fourthNum, it.fifthNum, it.sixthNum)
                }
            if (numbers == null) {
                repeat(6) { LottoBallPlaceholder(lottoTitle = "?", size = LottoBallLargeSize) }
            } else {
                numbers.forEach { number ->
                    LottoBall(
                        lottoType = LottoType.LOTTO645,
                        lottoColor = number.toLotteryColor(),
                        lottoTitle = number.toString(),
                        size = LottoBallLargeSize,
                    )
                }
            }
        } else {
            val random = state.pensionLotteryRandom
            if (random == null) {
                LottoGroupChip(group = "?", height = LottoBallLargeSize)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LottoSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(6) { LottoBallPlaceholder(lottoTitle = "?", size = LottoBallLargeSize) }
                }
            } else {
                LottoPensionBalls(
                    group = random.pensionGroup.toString(),
                    numbers =
                        listOf(
                            random.pensionFirstNum,
                            random.pensionSecondNum,
                            random.pensionThirdNum,
                            random.pensionFourthNum,
                            random.pensionFifthNum,
                            random.pensionSixthNum,
                        ).map { it.toString() },
                    size = LottoBallLargeSize,
                )
            }
        }
    }
}
