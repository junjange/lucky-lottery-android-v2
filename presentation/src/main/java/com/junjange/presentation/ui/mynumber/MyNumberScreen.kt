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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.junjange.domain.model.LotteryGetContent
import com.junjange.domain.model.LotteryGetNumbers
import com.junjange.domain.model.PensionLotteryGetContent
import com.junjange.domain.model.PensionLotteryNumbers
import com.junjange.presentation.R
import com.junjange.presentation.component.ExpandableActionButton
import com.junjange.presentation.component.LoadingDialog
import com.junjange.presentation.component.Lotto645Content
import com.junjange.presentation.component.Lotto720Content
import com.junjange.presentation.component.LottoContentTitle
import com.junjange.presentation.component.LottoNumberEntry
import com.junjange.presentation.component.PensionLotteryNumberEntry
import com.junjange.presentation.ui.mynumber.MyNumberContract.Effect.NavigateToGallery
import com.junjange.presentation.ui.mynumber.MyNumberContract.Event.*
import com.junjange.presentation.ui.theme.LottoTheme
import com.junjange.presentation.ui.theme.lotteryColors
import com.junjange.presentation.ui.theme.toLotteryColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyNumberScreen(viewModel: MyNumberViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tabs = listOf(R.string.lotto_645_title, R.string.lotto_720_title)
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val imageCropLauncher =
        rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent ?: return@rememberLauncherForActivityResult
                val imagePath =
                    result.getUriFilePath(context, false)
                        ?: return@rememberLauncherForActivityResult
                when (pagerState.currentPage) {
                    0 -> viewModel.event(LottoTextOfImage(imagePath = imagePath))
                    1 -> viewModel.event(PensionLottoTextOfImage(imagePath = imagePath))
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
            uri ?: return@rememberLauncherForActivityResult
            val cropOptions = CropImageContractOptions(uri, imageCropperOptions)
            imageCropLauncher.launch(cropOptions)
        }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NavigateToGallery -> imagePickerLauncher.launch("image/*")
            }
        }
    }

    MyNumberContent(
        tabs = tabs,
        pagerState = pagerState,
        lotteryGetContent = state.lotteryFlow,
        pensionLotteryGetContent = state.pensionLotteryFlow,
        onGalleryClicked = { viewModel.event(PickedImage) },
        onLotterySaveClicked = { lottery -> viewModel.event(InsertLottery(lottery)) },
        onPensionLotterySaveClicked = { pensionLottery ->
            viewModel.event(InsertPensionLottery(pensionLottery))
        },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyNumberContent(
    tabs: List<Int>,
    pagerState: PagerState,
    lotteryGetContent: Flow<PagingData<LotteryGetContent>>,
    pensionLotteryGetContent: Flow<PagingData<PensionLotteryGetContent>>,
    modifier: Modifier = Modifier,
    onGalleryClicked: () -> Unit,
    onLotterySaveClicked: (List<String>) -> Unit,
    onPensionLotterySaveClicked: (List<String>) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(modifier = modifier.fillMaxSize()) {
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
                    MyLotteryContentScreen(
                        lotteryGetContent = lotteryGetContent,
                        onEditClicked = { isSheetOpen = true },
                        onGalleryClicked = onGalleryClicked,
                    )

                1 ->
                    MyPensionLotteryContentScreen(
                        pensionLotteryGetContent = pensionLotteryGetContent,
                        onEditClicked = { isSheetOpen = true },
                        onGalleryClicked = onGalleryClicked,
                    )
            }
        }

        if (isSheetOpen) {
            ModalBottomSheet(
                modifier = Modifier.wrapContentHeight(),
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState,
            ) {
                when (pagerState.currentPage) {
                    0 ->
                        LottoNumberEntry(
                            onSaveClicked = { lottery ->
                                isSheetOpen = false
                                onLotterySaveClicked(lottery)
                            },
                        )

                    1 ->
                        PensionLotteryNumberEntry(
                            onSaveClicked = { pensionLottery ->
                                isSheetOpen = false
                                onPensionLotterySaveClicked(pensionLottery)
                            },
                        )
                }
            }
        }
    }
}

@Composable
fun MyLotteryContentScreen(
    lotteryGetContent: Flow<PagingData<LotteryGetContent>>,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
) {
    val contents = lotteryGetContent.collectAsLazyPagingItems()

    when (contents.loadState.refresh) {
        is LoadState.Error -> {}
        is LoadState.Loading -> LoadingDialog(modifier = Modifier.fillMaxSize())
        else -> {
            MyLotteryContent(
                contents = contents,
                onEditClicked = onEditClicked,
                onGalleryClicked = onGalleryClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyLotteryContent(
    contents: LazyPagingItems<LotteryGetContent>,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    val refreshState =
        rememberPullRefreshState(
            refreshing = contents.loadState.refresh is LoadState.Loading,
            onRefresh = { contents.refresh() },
        )

    val firstVisibleItemScrollOffset =
        remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .pullRefresh(refreshState),
    ) {
        LazyColumn(
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(contents.itemCount) {
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = LottoTheme.colors.gray200),
                    shape = RoundedCornerShape(size = 8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        contents[it]?.let { lotteryGetContent ->
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

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        PullRefreshIndicator(
            refreshing = contents.loadState.refresh is LoadState.Loading,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        ExpandableActionButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 15.dp, end = 15.dp),
            onEditClicked = onEditClicked,
            onGalleryClicked = onGalleryClicked,
            isFabExpanded = firstVisibleItemScrollOffset.value == 0,
        )
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
fun MyPensionLotteryContentScreen(
    pensionLotteryGetContent: Flow<PagingData<PensionLotteryGetContent>>,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
) {
    val contents = pensionLotteryGetContent.collectAsLazyPagingItems()

    when (contents.loadState.refresh) {
        is LoadState.Error -> {}
        is LoadState.Loading -> LoadingDialog(modifier = Modifier.fillMaxSize())
        else -> {
            MyPensionLotteryContent(
                contents = contents,
                onEditClicked = onEditClicked,
                onGalleryClicked = onGalleryClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyPensionLotteryContent(
    contents: LazyPagingItems<PensionLotteryGetContent>,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    val refreshState =
        rememberPullRefreshState(
            refreshing = contents.loadState.refresh is LoadState.Loading,
            onRefresh = { contents.refresh() },
        )

    val firstVisibleItemScrollOffset =
        remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .pullRefresh(refreshState),
    ) {
        LazyColumn(
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(contents.itemCount) {
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = LottoTheme.colors.gray200),
                    shape = RoundedCornerShape(size = 8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        contents[it]?.let { pensionLotteryGetContent ->
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

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        PullRefreshIndicator(
            refreshing = contents.loadState.refresh is LoadState.Loading,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        ExpandableActionButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 15.dp, end = 15.dp),
            onEditClicked = onEditClicked,
            onGalleryClicked = onGalleryClicked,
            isFabExpanded = firstVisibleItemScrollOffset.value == 0,
        )
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
    Text(
        modifier =
            Modifier
                .border(width = 1.dp, color = LottoTheme.colors.gray400)
                .fillMaxHeight()
                .weight(weight)
                .padding(8.8.dp),
        text = rank.toRankTitle(),
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
                lottoNumbers.group,
                lottoNumbers.firstNum,
                lottoNumbers.secondNum,
                lottoNumbers.thirdNum,
                lottoNumbers.fourthNum,
                lottoNumbers.fifthNum,
                lottoNumbers.sixthNum,
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
            val color = item.toLotteryColor()

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
