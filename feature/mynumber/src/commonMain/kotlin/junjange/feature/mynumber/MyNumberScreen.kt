package junjange.feature.mynumber

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.components.ErrorRetryScreen
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.designsystem.theme.lotteryColors
import junjange.core.designsystem.theme.toLotteryColor
import junjange.core.domain.model.LotteryGetContent
import junjange.core.domain.model.LotteryGetNumbers
import junjange.core.domain.model.PensionLotteryGetContent
import junjange.core.domain.model.PensionLotteryNumbers
import junjange.core.ui.component.EmptyScreen
import junjange.core.ui.component.ExpandableActionButton
import junjange.core.ui.component.Lotto645Content
import junjange.core.ui.component.Lotto720Content
import junjange.core.ui.component.LottoContentTitle
import junjange.core.ui.component.LottoNumberEntry
import junjange.core.ui.component.PensionLotteryNumberEntry
import junjange.feature.mynumber.MyNumberContract.Effect.*
import junjange.feature.mynumber.MyNumberContract.Event.*
import junjange.feature.mynumber.MyNumberContract.PagedContent
import junjange.feature.mynumber.MyNumberContract.PageLoadState
import junjange.feature.mynumber.MyNumberContract.UserRoundId
import junjange.feature.mynumber.dialog.LotteryDeleteDialog
import junjange.feature.mynumber.resources.Res
import junjange.feature.mynumber.resources.delete
import junjange.feature.mynumber.resources.delete_close
import junjange.feature.mynumber.resources.deselect_all
import junjange.feature.mynumber.resources.empty_delete_list
import junjange.feature.mynumber.resources.empty_lotto_description
import junjange.feature.mynumber.resources.empty_lotto_title
import junjange.feature.mynumber.resources.empty_pension_lotto_description
import junjange.feature.mynumber.resources.empty_pension_lotto_title
import junjange.feature.mynumber.resources.group_title
import junjange.feature.mynumber.resources.lottery_insert_failed
import junjange.feature.mynumber.resources.lotto_645_title
import junjange.feature.mynumber.resources.lotto_720_title
import junjange.feature.mynumber.resources.lotto_duplicate_error
import junjange.feature.mynumber.resources.lotto_number_submitted
import junjange.feature.mynumber.resources.pension_lottery_insert_failed
import junjange.feature.mynumber.resources.pension_lottery_invalid_group
import junjange.feature.mynumber.resources.pension_lottery_number_submitted
import junjange.feature.mynumber.resources.select_all
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun MyNumberScreen(
    viewModel: MyNumberViewModel,
    initialPage: Int,
) {
    val state by viewModel.state.collectAsState()
    val tabs = listOf(Res.string.lotto_645_title, Res.string.lotto_720_title)
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val launchImagePicker =
        rememberLotteryImagePicker { imagePath ->
            when (pagerState.currentPage) {
                0 -> viewModel.event(LottoTextOfImage(imagePath = imagePath))
                1 -> viewModel.event(PensionLottoTextOfImage(imagePath = imagePath))
            }
        }

    var isDeleteMode by remember { mutableStateOf(false) }
    var isAllSelected by remember { mutableStateOf(false) }

    val deleteLottery = remember { mutableStateListOf<UserRoundId>() }
    val deletePensionLottery = remember { mutableStateListOf<UserRoundId>() }

    val snackbarHostState = remember { SnackbarHostState() }

    // 탭 재진입/저장 복귀 시 최신 목록으로 갱신
    LaunchedEffect(Unit) {
        viewModel.event(RefreshLottery)
        viewModel.event(RefreshPensionLottery)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NavigateToGallery -> launchImagePicker()
                is ShowMessage -> {
                    val message =
                        when (effect.message) {
                            MyNumberMessage.LOTTERY_INSERT_FAILED -> getString(Res.string.lottery_insert_failed)
                            MyNumberMessage.PENSION_LOTTERY_INSERT_FAILED -> getString(Res.string.pension_lottery_insert_failed)
                            MyNumberMessage.LOTTERY_INSERT_SUCCESS -> getString(Res.string.lotto_number_submitted)
                            MyNumberMessage.PENSION_LOTTERY_INSERT_SUCCESS -> getString(Res.string.pension_lottery_number_submitted)
                        }
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    if (state.isDeleteLotteryDialogShowing) {
        LotteryDeleteDialog(
            onDismiss = { viewModel.event(ShowDialog(isDialogShowing = false)) },
            okClick = {
                if (isAllSelected) {
                    when (pagerState.currentPage) {
                        0 -> viewModel.event(DeleteAllLottery)
                        1 -> viewModel.event(DeleteAllPensionLottery)
                    }
                    deleteLottery.clear()
                    deletePensionLottery.clear()
                    isAllSelected = false
                } else {
                    if (deleteLottery.isNotEmpty()) {
                        viewModel.event(DeleteLottery(deleteLottery.toList()))
                        deleteLottery.clear()
                    }

                    if (deletePensionLottery.isNotEmpty()) {
                        viewModel.event(DeletePensionLottery(deletePensionLottery.toList()))
                        deletePensionLottery.clear()
                    }
                }

                viewModel.event(ShowDialog(isDialogShowing = false))
                isDeleteMode = false
            },
        )
    }

    MyNumberContent(
        tabs = tabs,
        pagerState = pagerState,
        lotteryContent = state.lottery,
        pensionLotteryContent = state.pensionLottery,
        deleteLottery = deleteLottery,
        deletePensionLottery = deletePensionLottery,
        isDeleteMode = isDeleteMode,
        snackbarHostState = snackbarHostState,
        onGalleryClicked = { viewModel.event(PickedImage) },
        onDeleteClicked = { deleteMode -> isDeleteMode = deleteMode },
        onLotterySaveClicked = { lotteries -> viewModel.event(InsertLotteries(lotteries)) },
        onPensionLotterySaveClicked = { pensionLotteries ->
            viewModel.event(InsertPensionLotteries(pensionLotteries))
        },
        onRefreshLottery = { viewModel.event(RefreshLottery) },
        onRefreshPensionLottery = { viewModel.event(RefreshPensionLottery) },
        onLoadMoreLottery = { viewModel.event(LoadMoreLottery) },
        onLoadMorePensionLottery = { viewModel.event(LoadMorePensionLottery) },
        checkedLottery = { lotteryGetNumber ->
            isAllSelected = false
            if (deleteLottery.contains(lotteryGetNumber)) {
                deleteLottery.remove(lotteryGetNumber)
            } else {
                deleteLottery.add(lotteryGetNumber)
            }
        },
        checkedPensionLottery = { pensionLotteryNumbers ->
            isAllSelected = false
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
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(getString(Res.string.empty_delete_list))
                }
            }
        },
        isAllSelected = isAllSelected,
        onSelectAllClicked = {
            if (isAllSelected) {
                deleteLottery.clear()
                deletePensionLottery.clear()
                isAllSelected = false
            } else {
                when (pagerState.currentPage) {
                    0 -> {
                        deleteLottery.clear()
                        state.lottery.items.forEach { content ->
                            content.lotteryGetNumbers.forEach { number ->
                                deleteLottery.add(UserRoundId(round = content.round, id = number.id))
                            }
                        }
                    }

                    1 -> {
                        deletePensionLottery.clear()
                        state.pensionLottery.items.forEach { content ->
                            content.pensionLotteryNumbers.forEach { number ->
                                deletePensionLottery.add(UserRoundId(round = content.round, id = number.id))
                            }
                        }
                    }
                }
                isAllSelected = true
            }
        },
        onDeleteLotteryCancelClicked = {
            isDeleteMode = false
            isAllSelected = false
            deleteLottery.clear()
            deletePensionLottery.clear()
        },
    )
}

@Composable
private fun LazyListState.OnLoadMore(onLoadMore: () -> Unit) {
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            lastVisible >= layoutInfo.totalItemsCount - 3
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNumberContent(
    tabs: List<StringResource>,
    pagerState: PagerState,
    lotteryContent: PagedContent<LotteryGetContent>,
    pensionLotteryContent: PagedContent<PensionLotteryGetContent>,
    deleteLottery: List<UserRoundId>,
    deletePensionLottery: List<UserRoundId>,
    isDeleteMode: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: (isDeleteMode: Boolean) -> Unit,
    onLotterySaveClicked: (List<List<String>>) -> Unit,
    onPensionLotterySaveClicked: (List<List<String>>) -> Unit,
    onRefreshLottery: () -> Unit,
    onRefreshPensionLottery: () -> Unit,
    onLoadMoreLottery: () -> Unit,
    onLoadMorePensionLottery: () -> Unit,
    checkedLottery: (userRoundId: UserRoundId) -> Unit,
    checkedPensionLottery: (userRoundId: UserRoundId) -> Unit,
    onDeleteLotteryClicked: () -> Unit,
    isAllSelected: Boolean,
    onSelectAllClicked: () -> Unit,
    onDeleteLotteryCancelClicked: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (isDeleteMode) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(vertical = 11.dp, horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(Res.string.delete_close),
                        modifier =
                            Modifier.clickable {
                                onDeleteLotteryCancelClicked()
                            },
                    )
                    Row {
                        Text(
                            text =
                                stringResource(
                                    if (isAllSelected) Res.string.deselect_all else Res.string.select_all,
                                ),
                            modifier =
                                Modifier.clickable {
                                    onSelectAllClicked()
                                },
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = stringResource(Res.string.delete),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                            modifier =
                                Modifier.clickable {
                                    onDeleteLotteryClicked()
                                },
                        )
                    }
                }
            } else {
                TabRow(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                    selectedTabIndex = pagerState.currentPage,
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(stringResource(title)) },
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
                            contents = lotteryContent,
                            isDeleteMode = isDeleteMode,
                            deleteLottery = deleteLottery,
                            onEditClicked = { isSheetOpen = true },
                            onGalleryClicked = onGalleryClicked,
                            onDeleteClicked = { onDeleteClicked(true) },
                            onRefresh = onRefreshLottery,
                            onLoadMore = onLoadMoreLottery,
                            checkedLottery = checkedLottery,
                        )

                    1 ->
                        MyPensionLotteryContent(
                            contents = pensionLotteryContent,
                            isDeleteMode = isDeleteMode,
                            deletePensionLottery = deletePensionLottery,
                            onEditClicked = { isSheetOpen = true },
                            onGalleryClicked = onGalleryClicked,
                            onDeleteClicked = { onDeleteClicked(true) },
                            onRefresh = onRefreshPensionLottery,
                            onLoadMore = onLoadMorePensionLottery,
                            checkedPensionLottery = checkedPensionLottery,
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
                                onSubmit = { lotteries ->
                                    isSheetOpen = false
                                    onLotterySaveClicked(lotteries)
                                },
                                onDuplicateLottery = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(getString(Res.string.lotto_duplicate_error))
                                    }
                                },
                            )

                        1 ->
                            PensionLotteryNumberEntry(
                                onSubmit = { pensionLotteries ->
                                    isSheetOpen = false
                                    onPensionLotterySaveClicked(pensionLotteries)
                                },
                                onInvalidGroup = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(getString(Res.string.pension_lottery_invalid_group))
                                    }
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
    contents: PagedContent<LotteryGetContent>,
    deleteLottery: List<UserRoundId>,
    isDeleteMode: Boolean,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    checkedLottery: (userRoundId: UserRoundId) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val refreshState = rememberPullToRefreshState()
    val firstVisibleItemScrollOffset =
        remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

    lazyListState.OnLoadMore(onLoadMore)

    PullToRefreshBox(
        isRefreshing = contents.isRefreshing,
        onRefresh = onRefresh,
        state = refreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = contents.isRefreshing,
                state = refreshState,
            )
        },
    ) {
        if (contents.loadState == PageLoadState.Error) {
            ErrorRetryScreen(
                title = "인터넷 연결이 불안정해요.",
                description = "Wi-Fi나 셀룰러 데이터 연결 상태를\n확인하고 다시 시도해주세요.",
                onRetry = onRefresh,
            )
            return@PullToRefreshBox
        }

        if (contents.items.isEmpty()) {
            if (contents.loadState == PageLoadState.Idle) {
                EmptyScreen(
                    title = stringResource(Res.string.empty_lotto_title),
                    description = stringResource(Res.string.empty_lotto_description),
                )
            }
        } else {
            LazyColumn(
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(contents.items.size) {
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                        shape = RoundedCornerShape(size = 8.dp),
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .padding(vertical = 18.dp)
                                    .padding(end = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            val lotteryGetContent = contents.items[it]
                            LottoContentTitle(
                                title = stringResource(Res.string.lotto_645_title),
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

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isDeleteMode) {
                Checkbox(
                    modifier = Modifier.height(40.dp),
                    checked = deleteLottery.contains(userRoundId),
                    onCheckedChange = {
                        checkedLottery(userRoundId)
                    },
                )
            } else {
                Spacer(modifier = Modifier.width(18.dp))
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
    contents: PagedContent<PensionLotteryGetContent>,
    deletePensionLottery: List<UserRoundId>,
    isDeleteMode: Boolean,
    checkedPensionLottery: (userRoundId: UserRoundId) -> Unit,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val refreshState = rememberPullToRefreshState()
    val firstVisibleItemScrollOffset =
        remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

    lazyListState.OnLoadMore(onLoadMore)

    PullToRefreshBox(
        isRefreshing = contents.isRefreshing,
        onRefresh = onRefresh,
        state = refreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = contents.isRefreshing,
                state = refreshState,
            )
        },
    ) {
        if (contents.loadState == PageLoadState.Error) {
            ErrorRetryScreen(
                title = "인터넷 연결이 불안정해요.",
                description = "Wi-Fi나 셀룰러 데이터 연결 상태를\n확인하고 다시 시도해주세요.",
                onRetry = onRefresh,
            )
            return@PullToRefreshBox
        }
        if (contents.items.isEmpty()) {
            if (contents.loadState == PageLoadState.Idle) {
                EmptyScreen(
                    title = stringResource(Res.string.empty_pension_lotto_title),
                    description = stringResource(Res.string.empty_pension_lotto_description),
                )
            }
        } else {
            LazyColumn(
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(contents.items.size) {
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                        shape = RoundedCornerShape(size = 8.dp),
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .padding(vertical = 18.dp)
                                    .padding(end = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            val pensionLotteryGetContent = contents.items[it]
                            LottoContentTitle(
                                title = stringResource(Res.string.lotto_720_title),
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

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isDeleteMode) {
                Checkbox(
                    modifier = Modifier.height(40.dp),
                    checked = deletePensionLottery.contains(userRounds),
                    onCheckedChange = {
                        checkedPensionLottery(userRounds)
                    },
                )
            } else {
                Spacer(modifier = Modifier.width(18.dp))
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
    Box(
        modifier =
            Modifier
                .weight(weight)
                .height(40.dp)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = rank.toRankTitle(),
            style = LottoTheme.typography.body3,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                .weight(weight)
                .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
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
fun RowScope.TableCell(
    lottoNumbers: PensionLotteryNumbers,
    checkWinningBonus: Boolean,
    weight: Float,
) {
    Row(
        modifier =
            Modifier
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                .weight(weight)
                .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
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
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(Res.string.group_title),
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
fun MyLotteryBall(
    isSuccess: Boolean,
    lottoTitle: String,
    color: androidx.compose.ui.graphics.Color,
) {
    val backgroundColor = if (isSuccess) color else MaterialTheme.colorScheme.surfaceContainerHighest
    val textColor = if (isSuccess) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface

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
                color = if (index == null) MaterialTheme.colorScheme.surfaceContainerHighest else lotteryColors[index],
            )
        } else {
            null
        }
    val backgroundColor = if (isSuccess) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainerHighest
    val textColor = MaterialTheme.colorScheme.onSurface

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
