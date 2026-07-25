package junjange.feature.randomnumbergeneration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import junjange.core.designsystem.theme.BallNeutral
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.lotteryColors
import junjange.core.designsystem.theme.toLotteryColor
import junjange.core.domain.model.LottoType
import junjange.core.ui.component.LottoBall
import junjange.core.ui.component.LottoRoundedCornerButton
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
                            actionLabel = "",
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
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = title,
                    style = LottoTheme.typography.headline3,
                )
            }, navigationIcon = {
                IconButton(
                    onClick = { viewModel.event(Event.Back) },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_chevron_left),
                        contentDescription = null,
                    )
                }
            })
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
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            PlatformAdBanner(modifier = Modifier.fillMaxWidth())
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RandomNumberGenerationContent(
                    state = state,
                    onCreateClicked = {
                        if (state.isLotto645) {
                            viewModel.event(Event.GenerateRandomLottery)
                        } else {
                            viewModel.event(Event.GenerateRandomPensionLottery)
                        }
                    },
                    onSaveClicked = {
                        if (state.isLotto645) {
                            viewModel.event(Event.SaveLottery)
                        } else {
                            viewModel.event(Event.SavePensionLottery)
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun RandomNumberGenerationContent(
    state: State,
    onCreateClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    Image(
        modifier = Modifier.size(140.dp),
        painter = painterResource(Res.drawable.ic_random_poster),
        contentDescription = null,
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = stringResource(if (state.isLotto645) Res.string.lotto_645_title else Res.string.lotto_720_title),
        style = LottoTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
    )

    Spacer(modifier = Modifier.height(30.dp))

    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (state.isLotto645) {
            state.lotteryRandomNumbers?.let {
                listOf(it.firstNum, it.secondNum, it.thirdNum, it.fourthNum, it.fifthNum, it.sixthNum)
                    .forEach { number ->
                        LottoBall(lottoType = LottoType.LOTTO645, lottoColor = number.toLotteryColor(), lottoTitle = number.toString())
                        Spacer(modifier = Modifier.width(4.dp))
                    }
            } ?: run {
                List(6) { 0 }.forEach { number ->
                    LottoBall(lottoType = LottoType.LOTTO645, lottoColor = BallNeutral, lottoTitle = number.toString())
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        } else {
            state.pensionLotteryRandom?.let {
                listOf(it.pensionGroup, it.pensionFirstNum, it.pensionSecondNum, it.pensionThirdNum, it.pensionFourthNum, it.pensionFourthNum, it.pensionSixthNum)
                    .forEachIndexed { index, s ->
                        if (index == 1) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = stringResource(Res.string.group_title), style = LottoTheme.typography.headline3)
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        LottoBall(lottoType = LottoType.LOTTO720, lottoColor = lotteryColors[index], lottoTitle = s.toString())
                        Spacer(modifier = Modifier.width(4.dp))
                    }
            } ?: run {
                List(7) { 0 }.forEachIndexed { index, s ->
                    if (index == 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = stringResource(Res.string.group_title), style = LottoTheme.typography.headline3)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    LottoBall(lottoType = LottoType.LOTTO720, lottoColor = lotteryColors[index], lottoTitle = s.toString())
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Row(
        modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LottoRoundedCornerButton(
            modifier = Modifier.clip(shape = RoundedCornerShape(8.dp)).height(40.dp).width(140.dp),
            buttonText = stringResource(Res.string.create_title),
            isEnabled = true,
            onClick = { onCreateClicked() },
        )
        Spacer(modifier = Modifier.width(20.dp))
        LottoRoundedCornerButton(
            modifier = Modifier.clip(shape = RoundedCornerShape(8.dp)).height(40.dp).width(140.dp),
            buttonText = stringResource(Res.string.save_title),
            isEnabled = state.saveIsEnabled,
            onClick = { onSaveClicked() },
        )
    }
}
