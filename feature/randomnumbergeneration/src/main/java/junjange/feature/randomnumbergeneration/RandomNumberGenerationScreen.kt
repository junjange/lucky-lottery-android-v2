package junjange.feature.randomnumbergeneration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.lotteryColors
import junjange.core.designsystem.theme.toLotteryColor
import junjange.core.domain.model.LottoType
import junjange.core.ui.component.AdmobBanner
import junjange.core.ui.component.LottoBall
import junjange.core.ui.component.LottoRoundedCornerButton
import junjange.feature.randomnumbergeneration.RandomNumberGenerationContract.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RandomNumberGenerationScreen(
    viewModel: RandomNumberGenerationViewModel,
    navigateToMain: (initialPage: String) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.Finish -> onBack()
                is Effect.ShowMessage -> {
                    val (message, initialPage) =
                        when (effect.message) {
                            RandomNumberGenerationMessage.LOTTERY_NUMBER_SAVED ->
                                Pair(R.string.lotto_number_submitted, "0")

                            RandomNumberGenerationMessage.PENSION_LOTTERY_SAVED ->
                                Pair(R.string.pension_lottery_number_submitted, "1")
                        }
                    val result =
                        snackbarHostState.showSnackbar(
                            message = context.getString(message),
                            actionLabel = context.getString(R.string.action_check_number),
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
            stringResource(R.string.lotto_645_random_title)
        } else {
            stringResource(
                R.string.lotto_720_title,
            )
        }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = title,
                    style = LottoTheme.typography.headline3,
                )
            }, navigationIcon = {
                IconButton(
                    onClick = {
                        viewModel.event(Event.Back)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_left),
                        contentDescription = null,
                    )
                }
            })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = LottoTheme.colors.green,
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
            AdmobBanner(modifier = Modifier.fillMaxWidth())
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
        painter = painterResource(id = R.drawable.ic_random_poster),
        contentDescription = null,
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = stringResource(id = if (state.isLotto645) R.string.lotto_645_title else R.string.lotto_720_title),
        style = LottoTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
    )

    Spacer(modifier = Modifier.height(30.dp))

    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (state.isLotto645) {
            state.lotteryRandomNumbers?.let {
                listOf(
                    it.firstNum,
                    it.secondNum,
                    it.thirdNum,
                    it.fourthNum,
                    it.fifthNum,
                    it.sixthNum,
                ).forEach { number ->
                    val color = number.toLotteryColor()

                    LottoBall(
                        lottoType = LottoType.LOTTO645,
                        lottoColor = color,
                        lottoTitle = number.toString(),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            } ?: run {
                List(6) { 0 }.forEach { number ->
                    LottoBall(
                        lottoType = LottoType.LOTTO645,
                        lottoColor = LottoTheme.colors.gray400,
                        lottoTitle = number.toString(),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        } else {
            state.pensionLotteryRandom?.let {
                listOf(
                    it.pensionGroup,
                    it.pensionFirstNum,
                    it.pensionSecondNum,
                    it.pensionThirdNum,
                    it.pensionFourthNum,
                    it.pensionFourthNum,
                    it.pensionSixthNum,
                ).forEachIndexed { index, s ->
                    if (index == 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.group_title),
                            style = LottoTheme.typography.headline3,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    LottoBall(
                        lottoType = LottoType.LOTTO720,
                        lottoColor = lotteryColors[index],
                        lottoTitle = s.toString(),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            } ?: run {
                List(7) { 0 }.forEachIndexed { index, s ->
                    if (index == 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.group_title),
                            style = LottoTheme.typography.headline3,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    LottoBall(
                        lottoType = LottoType.LOTTO720,
                        lottoColor = lotteryColors[index],
                        lottoTitle = s.toString(),
                    )
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
            modifier =
                Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .height(40.dp)
                    .width(140.dp),
            buttonText = stringResource(R.string.create_title),
            backgroundColor = LottoTheme.colors.green,
            isEnabled = true,
            onClick = { onCreateClicked() },
        )
        Spacer(modifier = Modifier.width(20.dp))
        LottoRoundedCornerButton(
            modifier =
                Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .height(40.dp)
                    .width(140.dp),
            buttonText = stringResource(R.string.save_title),
            backgroundColor = LottoTheme.colors.green,
            isEnabled = state.saveIsEnabled,
            onClick = {
                onSaveClicked()
            },
        )
    }
}
