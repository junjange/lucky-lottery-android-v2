package com.junjange.presentation.ui.mynumber

import android.graphics.Color
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.junjange.presentation.component.Lotto645Content
import com.junjange.presentation.component.Lotto720Content
import com.junjange.presentation.component.LottoContentTitle
import com.junjange.presentation.component.LottoNumberEntry
import com.junjange.presentation.component.PensionLotteryNumberEntry
import com.junjange.presentation.theme.LottoTheme
import com.junjange.presentation.theme.lotteryColors
import com.junjange.presentation.theme.toLotteryColor
import com.junjange.presentation.ui.dialog.LotteryDeleteDialog
import com.junjange.presentation.ui.mynumber.MyNumberContract.Effect.*
import com.junjange.presentation.ui.mynumber.MyNumberContract.Event.*
import com.junjange.presentation.ui.mynumber.MyNumberContract.UserRoundId
import com.junjange.presentation.util.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MyNumberScreen(
    viewModel: MyNumberViewModel = hiltViewModel(),
    initialPage: Int,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tabs = listOf(R.string.lotto_645_title, R.string.lotto_720_title)
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { tabs.size })

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

    val lotteryGetContent = state.lotteryFlow.collectAsLazyPagingItems()
    val pensionLotteryGetContent = state.pensionLotteryFlow.collectAsLazyPagingItems()
    var isDeleteMode by remember { mutableStateOf(false) }

    val deleteLottery =
        rememberSaveable(
            saver =
                listSaver(
                    save = { it.toList() },
                    restore = { mutableStateListOf(*it.toTypedArray()) },
                ),
        ) {
            mutableStateListOf<UserRoundId>()
        }

    val deletePensionLottery =
        rememberSaveable(
            saver =
                listSaver(
                    save = { it.toList() },
                    restore = { mutableStateListOf(*it.toTypedArray()) },
                ),
        ) {
            mutableStateListOf<UserRoundId>()
        }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NavigateToGallery -> imagePickerLauncher.launch("image/*")
                is LotteryRefresh -> lotteryGetContent.refresh()
                is PensionLotteryRefresh -> pensionLotteryGetContent.refresh()
            }
        }
    }

    if (state.isDeleteLotteryDialogShowing) {
        LotteryDeleteDialog(
            onDismiss = { viewModel.event(ShowDialog(isDialogShowing = false)) },
            okClick = {
                if (deleteLottery.isNotEmpty()) {
                    viewModel.event(DeleteLottery(deleteLottery.toList()))
                }

                if (deletePensionLottery.isNotEmpty()) {
                    viewModel.event(DeletePensionLottery(deletePensionLottery.toList()))
                }

                viewModel.event(ShowDialog(isDialogShowing = false))
                isDeleteMode = false
            },
        )
    }

    MyNumberContent(
        tabs = tabs,
        pagerState = pagerState,
        lotteryGetContent = lotteryGetContent,
        pensionLotteryGetContent = pensionLotteryGetContent,
        deleteLottery = deleteLottery,
        deletePensionLottery = deletePensionLottery,
        isDeleteMode = isDeleteMode,
        onGalleryClicked = { viewModel.event(PickedImage) },
        onDeleteClicked = { deleteMode ->
            isDeleteMode = deleteMode
        },
        onLotterySaveClicked = { lottery -> viewModel.event(InsertLottery(lottery)) },
        onPensionLotterySaveClicked = { pensionLottery ->
            viewModel.event(InsertPensionLottery(pensionLottery))
        },
        checkedLottery = { lotteryGetNumber ->
            if (deleteLottery.contains(lotteryGetNumber)) {
                deleteLottery.remove(lotteryGetNumber)
            } else {
                deleteLottery.add(lotteryGetNumber)
            }
        },
        checkedPensionLottery = { pensionLotteryNumbers ->
            if (deletePensionLottery.contains(pensionLotteryNumbers)) {
                deletePensionLottery.remove(pensionLotteryNumbers)
            } else {
                deletePensionLottery.add(pensionLotteryNumbers)
            }
        },
        onDeleteLotteryClicked = {
            if (deleteLottery.isNotEmpty() || deletePensionLottery.isNotEmpty()) {
                viewModel.event(ShowDialog(isDialogShowing = true))
            } else {
                context.showToast(context.getString(R.string.empty_delete_list))
            }
        },
        onDeleteLotteryCancelClicked = {
            isDeleteMode = false
            deleteLottery.clear()
            deletePensionLottery.clear()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNumberContent(
    tabs: List<Int>,
    pagerState: PagerState,
    lotteryGetContent: LazyPagingItems<LotteryGetContent>,
    pensionLotteryGetContent: LazyPagingItems<PensionLotteryGetContent>,
    deleteLottery: List<UserRoundId>,
    deletePensionLottery: List<UserRoundId>,
    isDeleteMode: Boolean,
    modifier: Modifier = Modifier,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: (isDeleteMode: Boolean) -> Unit,
    onLotterySaveClicked: (List<String>) -> Unit,
    onPensionLotterySaveClicked: (List<String>) -> Unit,
    checkedLottery: (userRoundId: UserRoundId) -> Unit,
    checkedPensionLottery: (userRoundId: UserRoundId) -> Unit,
    onDeleteLotteryClicked: () -> Unit,
    onDeleteLotteryCancelClicked: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (isDeleteMode) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.delete_close),
                        modifier =
                            Modifier.clickable {
                                onDeleteLotteryCancelClicked()
                            },
                    )
                    Text(
                        text = stringResource(R.string.delete),
                        color = LottoTheme.colors.lottoError,
                        modifier =
                            Modifier.clickable {
                                onDeleteLotteryClicked()
                            },
                    )
                }
            } else {
                TabRow(selectedTabIndex = pagerState.currentPage) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(stringResource(id = title)) },
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.scrollToPage(index) } },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = !isDeleteMode,
            ) { page ->
                when (page) {
                    0 ->
                        MyLotteryContent(
                            contents = lotteryGetContent,
                            isDeleteMode = isDeleteMode,
                            deleteLottery = deleteLottery,
                            onEditClicked = { isSheetOpen = true },
                            onGalleryClicked = onGalleryClicked,
                            onDeleteClicked = { onDeleteClicked(true) },
                            checkedLottery = checkedLottery,
                        )

                    1 ->
                        MyPensionLotteryContent(
                            contents = pensionLotteryGetContent,
                            isDeleteMode = isDeleteMode,
                            deletePensionLottery = deletePensionLottery,
                            onEditClicked = { isSheetOpen = true },
                            onGalleryClicked = onGalleryClicked,
                            onDeleteClicked = { onDeleteClicked(true) },
                            checkedPensionLottery = checkedPensionLottery,
                        )
                }
            }

            if (isSheetOpen) {
                ModalBottomSheet(
                    modifier = Modifier.wrapContentHeight(),
                    onDismissRequest = { isSheetOpen = false },
                    containerColor = LottoTheme.colors.lottoWhite,
                    sheetState = sheetState,
                ) {
                    when (pagerState.currentPage) {
                        0 ->
                            LottoNumberEntry(
                                onSubmit = { lottery ->
                                    isSheetOpen = false
                                    onLotterySaveClicked(lottery)
                                    context.showToast(R.string.lotto_number_submitted)
                                },
                                onDuplicateLottery = {
                                    context.showToast(R.string.lotto_duplicate_error)
                                },
                            )

                        1 ->
                            PensionLotteryNumberEntry(
                                onSubmit = { pensionLottery ->
                                    isSheetOpen = false
                                    onPensionLotterySaveClicked(pensionLottery)
                                    context.showToast(R.string.pension_lottery_number_submitted)
                                },
                                onInvalidGroup = {
                                    context.showToast(R.string.pension_lottery_invalid_group)
                                },
                            )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLotteryContent(
    contents: LazyPagingItems<LotteryGetContent>,
    deleteLottery: List<UserRoundId>,
    isDeleteMode: Boolean,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    checkedLottery: (userRoundId: UserRoundId) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val refreshState = rememberPullToRefreshState()
    val firstVisibleItemScrollOffset =
        remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

    PullToRefreshBox(
        isRefreshing = contents.loadState.refresh is LoadState.Loading,
        onRefresh = { contents.refresh() },
        state = refreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = contents.loadState.refresh is LoadState.Loading,
                containerColor = LottoTheme.colors.white,
                color = LottoTheme.colors.black,
                state = refreshState,
            )
        },
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
                            MyLotteryNumber(
                                round = lotteryGetContent.round,
                                lotteryGetNumbers = lotteryGetContent.lotteryGetNumbers,
                                isDeleteMode = isDeleteMode,
                                deleteLottery = deleteLottery,
                                checkedLottery = checkedLottery,
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        if (!isDeleteMode) {
            ExpandableActionButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 15.dp, end = 15.dp),
                onEditClicked = onEditClicked,
                onGalleryClicked = onGalleryClicked,
                isFabExpanded = firstVisibleItemScrollOffset.value == 0,
                onDeleteClicked = onDeleteClicked,
            )
        }
    }
}

@Composable
fun MyLotteryNumber(
    round: Int,
    lotteryGetNumbers: List<LotteryGetNumbers>,
    isDeleteMode: Boolean,
    deleteLottery: List<UserRoundId>,
    checkedLottery: (userRoundId: UserRoundId) -> Unit,
) {
    lotteryGetNumbers.forEach { lotteryGetNumber ->
        val userRoundId = UserRoundId(round = round, id = lotteryGetNumber.id)

        Row(Modifier.fillMaxWidth()) {
            if (isDeleteMode) {
                Checkbox(
                    checked = deleteLottery.contains(userRoundId),
                    onCheckedChange = {
                        checkedLottery(userRoundId)
                    },
                )
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPensionLotteryContent(
    contents: LazyPagingItems<PensionLotteryGetContent>,
    deletePensionLottery: List<UserRoundId>,
    isDeleteMode: Boolean,
    checkedPensionLottery: (userRoundId: UserRoundId) -> Unit,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val refreshState = rememberPullToRefreshState()
    val firstVisibleItemScrollOffset =
        remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

    PullToRefreshBox(
        isRefreshing = contents.loadState.refresh is LoadState.Loading,
        onRefresh = { contents.refresh() },
        state = refreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = contents.loadState.refresh is LoadState.Loading,
                containerColor = LottoTheme.colors.white,
                color = LottoTheme.colors.black,
                state = refreshState,
            )
        },
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
                                deletePensionLottery = deletePensionLottery,
                                isDeleteMode = isDeleteMode,
                                checkedPensionLottery = checkedPensionLottery,
                                round = pensionLotteryGetContent.round,
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        if (!isDeleteMode) {
            ExpandableActionButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 15.dp, end = 15.dp),
                onEditClicked = onEditClicked,
                onGalleryClicked = onGalleryClicked,
                onDeleteClicked = onDeleteClicked,
                isFabExpanded = firstVisibleItemScrollOffset.value == 0,
            )
        }
    }
}

@Composable
fun MyPensionLotteryNumber(
    round: Int,
    pensionLotteryNumbers: List<PensionLotteryNumbers>,
    deletePensionLottery: List<UserRoundId>,
    checkWinningBonus: Boolean,
    isDeleteMode: Boolean,
    checkedPensionLottery: (userRoundId: UserRoundId) -> Unit,
) {
    pensionLotteryNumbers.forEach { pensionLotteryNumber ->
        val userRounds = UserRoundId(round = round, id = pensionLotteryNumber.id)

        Row(Modifier.fillMaxWidth()) {
            if (isDeleteMode) {
                Checkbox(
                    checked = deletePensionLottery.contains(userRounds),
                    onCheckedChange = {
                        checkedPensionLottery(userRounds)
                    },
                )
            }

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
