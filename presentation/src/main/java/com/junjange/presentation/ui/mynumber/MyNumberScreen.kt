package com.junjange.presentation.ui.mynumber

import android.graphics.Color
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.junjange.domain.model.LotteryGetNumbers
import com.junjange.domain.model.PensionLotteryNumbers
import com.junjange.presentation.R
import com.junjange.presentation.component.Lotto645Content
import com.junjange.presentation.component.Lotto720Content
import com.junjange.presentation.component.LottoContentTitle
import com.junjange.presentation.ui.mynumber.MyNumberEffect.NavigateToGallery
import com.junjange.presentation.ui.theme.LottoTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyNumberScreen(
    viewModel: MyNumberViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    with(viewModel) {
                        getLotteryGet()
                        getPensionLotteryGet()
                    }
                }
            }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val tabs = listOf(R.string.lotto_645_title, R.string.lotto_720_title)
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val imageCropLauncher =
        rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent?.let {
                    if (pagerState.currentPage == 0) {
                        viewModel.getLottoTextOfImage(result.getUriFilePath(context, false)!!)
                    } else {
                        viewModel.getPensionLottoTextOfImage(result.getUriFilePath(context, false)!!)
                    }
                }
            }
        }

    val imageCropperOptions =
        CropImageOptions(
            cropShape = CropImageView.CropShape.RECTANGLE,
            fixAspectRatio = false,
            aspectRatioX = 1,
            aspectRatioY = 1,
            toolbarColor = Color.WHITE,
            toolbarBackButtonColor = Color.BLACK,
            toolbarTintColor = Color.BLACK,
            allowFlipping = false,
            allowRotation = false,
            cropMenuCropButtonTitle = context.getString(R.string.done),
            imageSourceIncludeCamera = false,
        )

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val cropOptions = CropImageContractOptions(it, imageCropperOptions)
                imageCropLauncher.launch(cropOptions)
            }
        }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NavigateToGallery -> imagePickerLauncher.launch("image/*")
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(stringResource(id = title)) },
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.scrollToPage(index) } },
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 ->
                    MyLotteryContent(
                        uiState = uiState,
                        viewModel = viewModel,
                    )

                1 ->
                    MyPensionLotteryContent(
                        uiState = uiState,
                        viewModel = viewModel,
                    )
            }
        }
    }
}

@Composable
fun MyPensionLotteryContent(
    uiState: MyNumberState,
    viewModel: MyNumberViewModel,
) {
    val pensionLotteryGetContent = uiState.pensionLotteryGetContent.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(pensionLotteryGetContent.itemCount) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier.padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = LottoTheme.colors.gray200),
                    shape = RoundedCornerShape(size = 8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        pensionLotteryGetContent[it]?.let { pensionLotteryGetContent ->
                            LottoContentTitle(
                                title = stringResource(R.string.lotto_720_title),
                                round = pensionLotteryGetContent.round,
                                winningDate = pensionLotteryGetContent.winningDate,
                            )
                            pensionLotteryGetContent.winningPensionLotteryNumbers?.let { winningPensionLotteryNumbers ->
                                pensionLotteryGetContent.winningPensionLotteryBonusNumbers?.let { winningPensionLotteryBonusNumbers ->
                                    Lotto720Content(
                                        winningPensionLotteryNumbers = winningPensionLotteryNumbers,
                                        winningPensionLotteryBonusNumbers = winningPensionLotteryBonusNumbers,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            MyPensionLotteryNumber(
                                pensionLotteryNumbers = pensionLotteryGetContent.pensionLotteryNumbers,
                                checkWinningBonus = pensionLotteryGetContent.checkWinningBonus,
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 15.dp, end = 15.dp),
            containerColor = LottoTheme.colors.green,
            onClick = { viewModel.onPickedImage() },
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = LottoTheme.colors.white,
            )
        }
    }
}

@Composable
fun MyLotteryContent(
    uiState: MyNumberState,
    viewModel: MyNumberViewModel,
) {
    val lotteryGetContent = uiState.lotteryGetContent.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(lotteryGetContent.itemCount) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier.padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = LottoTheme.colors.gray200),
                    shape = RoundedCornerShape(size = 8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        lotteryGetContent[it]?.let { lotteryGetContent ->
                            LottoContentTitle(
                                title = stringResource(R.string.lotto_645_title),
                                round = lotteryGetContent.round,
                                winningDate = lotteryGetContent.winningDate,
                            )
                            lotteryGetContent.winningLotteryNumbers?.let { winningLotteryNumbers ->
                                Lotto645Content(winningLotteryNumbers = winningLotteryNumbers)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            MyLotteryNumber(lotteryGetNumbers = lotteryGetContent.lotteryGetNumbers)
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 15.dp, end = 15.dp),
            containerColor = LottoTheme.colors.green,
            onClick = { viewModel.onPickedImage() },
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = LottoTheme.colors.white,
            )
        }
    }
}

@Composable
fun MyLotteryNumber(lotteryGetNumbers: List<LotteryGetNumbers>) {
    lotteryGetNumbers.forEach { lotteryGetNumber ->
        Row(Modifier.fillMaxWidth()) {
            TableCell(
                rank = lotteryGetNumber.rank,
                weight = 2f,
            )
            TableCell(
                lottoNumbers = lotteryGetNumber,
                weight = 8f,
            )
        }
    }
}

@Composable
fun MyPensionLotteryNumber(
    pensionLotteryNumbers: List<PensionLotteryNumbers>,
    checkWinningBonus: Boolean,
) {
    pensionLotteryNumbers.forEach { pensionLotteryNumber ->
        Row(Modifier.fillMaxWidth()) {
            TableCell(
                rank = pensionLotteryNumber.rank,
                weight = 2f,
            )
            TableCell(
                lottoNumbers = pensionLotteryNumber,
                checkWinningBonus = checkWinningBonus,
                weight = 8f,
            )
        }
    }
}

@Composable
fun RowScope.TableCell(
    rank: String?,
    weight: Float,
) {
    val title =
        when (rank) {
            "FIRST" -> "1등"
            "SECOND" -> "2등"
            "THIRD" -> "3등"
            "FOURTH" -> "4등"
            "FIFTH" -> "5등"
            "SIXTH" -> "6등"
            "SEVENTH" -> "7등"
            "NONE" -> "꽝"
            else -> "미발표"
        }

    Text(
        modifier =
            Modifier
                .border(width = 1.dp, color = LottoTheme.colors.gray400)
                .fillMaxHeight()
                .weight(weight)
                .padding(8.8.dp),
        text = title,
        style = LottoTheme.typography.body3,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun RowScope.TableCell(
    lottoNumbers: PensionLotteryNumbers,
    checkWinningBonus: Boolean,
    weight: Float,
) {
    Row(
        modifier =
            Modifier
                .border(width = 1.dp, color = LottoTheme.colors.gray400)
                .weight(weight)
                .padding(4.5.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        val lottoTitle =
            listOf(
                lottoNumbers.pensionGroup,
                lottoNumbers.pensionFirstNum,
                lottoNumbers.pensionSecondNum,
                lottoNumbers.pensionThirdNum,
                lottoNumbers.pensionFourthNum,
                lottoNumbers.pensionFifthNum,
                lottoNumbers.pensionSixthNum,
            ).map { it.toString() }

        lottoTitle.forEachIndexed { index, title ->
            if (index == 1) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    color = LottoTheme.colors.gray200,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(id = R.string.group_title),
                            textAlign = TextAlign.Center,
                            style = LottoTheme.typography.body3.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }

            if (checkWinningBonus) {
                MyPensionLotteryBall(
                    isSuccess = true,
                    lottoTitle = title,
                )
            } else {
                MyPensionLotteryBall(
                    isSuccess = if (lottoNumbers.correctNumbers == null) false else lottoNumbers.correctNumbers!![index],
                    lottoTitle = title,
                    index = index,
                )
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    lottoNumbers: LotteryGetNumbers,
    weight: Float,
) {
    Row(
        modifier =
            Modifier
                .border(width = 1.dp, color = LottoTheme.colors.gray400)
                .weight(weight)
                .padding(4.5.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        val lottoNumbersContent =
            listOf(
                lottoNumbers.firstNum,
                lottoNumbers.secondNum,
                lottoNumbers.thirdNum,
                lottoNumbers.fourthNum,
                lottoNumbers.fifthNum,
                lottoNumbers.sixthNum,
            )

        lottoNumbersContent.forEachIndexed { index, item ->
            val color =
                when (item) {
                    in 1..10 -> LottoTheme.colors.lottoYellow
                    in 11..20 -> LottoTheme.colors.lottoBlue
                    in 21..30 -> LottoTheme.colors.lottoError
                    in 31..40 -> LottoTheme.colors.gray400
                    in 41..45 -> LottoTheme.colors.lottoGreen
                    else -> LottoTheme.colors.lottoPurple
                }

            MyLotteryBall(
                isSuccess = if (lottoNumbers.correctNumbers == null) false else lottoNumbers.correctNumbers!![index],
                lottoTitle = item.toString(),
                color = color,
            )
        }
    }
}

@Composable
fun MyLotteryBall(
    isSuccess: Boolean,
    lottoTitle: String,
    color: androidx.compose.ui.graphics.Color,
) {
    val backgroundColor = if (isSuccess) color else LottoTheme.colors.gray200
    val textColor = if (isSuccess) LottoTheme.colors.white else LottoTheme.colors.black

    Surface(
        modifier = Modifier.size(30.dp),
        shape = CircleShape,
        color = backgroundColor,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = lottoTitle,
                textAlign = TextAlign.Center,
                style =
                    LottoTheme.typography.body3.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                    ),
            )
        }
    }

    Spacer(modifier = Modifier.width(4.dp))
}

@Composable
fun MyPensionLotteryBall(
    isSuccess: Boolean,
    lottoTitle: String,
    index: Int? = null,
) {
    val lotteryColors =
        listOf(
            LottoTheme.colors.gray600,
            LottoTheme.colors.lottoError,
            LottoTheme.colors.lottoOrange,
            LottoTheme.colors.lottoYellow,
            LottoTheme.colors.lottoBlue,
            LottoTheme.colors.lottoPurple,
            LottoTheme.colors.lottoBlack,
        )

    val borderColor =
        if (isSuccess) {
            BorderStroke(
                width = 4.dp,
                color = if (index == null) LottoTheme.colors.gray200 else lotteryColors[index],
            )
        } else {
            null
        }
    val backgroundColor = if (isSuccess) LottoTheme.colors.white else LottoTheme.colors.gray200
    val textColor = if (isSuccess) LottoTheme.colors.black else LottoTheme.colors.black

    Surface(
        modifier = Modifier.size(30.dp),
        border = borderColor,
        shape = CircleShape,
        color = backgroundColor,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = lottoTitle,
                textAlign = TextAlign.Center,
                style =
                    LottoTheme.typography.body3.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                    ),
            )
        }
    }

    if (index != 0 && isSuccess) Spacer(modifier = Modifier.width(4.dp))
}
