package junjange.feature.mynumber

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import junjange.core.designsystem.components.ErrorRetryScreen
import junjange.core.designsystem.theme.LottoShapeTokens
import junjange.core.designsystem.theme.LottoShapes
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.designsystem.theme.toLotteryColor
import junjange.core.domain.model.LotteryGetContent
import junjange.core.domain.model.LotteryGetNumbers
import junjange.core.domain.model.LottoType
import junjange.core.domain.model.PensionLotteryGetContent
import junjange.core.domain.model.PensionLotteryNumbers
import junjange.core.ui.component.EmptyScreen
import junjange.core.ui.component.NumberActionFab
import junjange.core.ui.component.LotteryCard
import junjange.core.ui.component.Lotto645WinningSection
import junjange.core.ui.component.LottoGroupChip
import junjange.core.ui.component.LottoNumberEntry
import junjange.core.ui.component.LottoNumberSection
import junjange.core.ui.component.LottoPensionBalls
import junjange.core.ui.component.PensionLotteryNumberEntry
import junjange.core.ui.component.lotteryRoundSubtitle
import junjange.core.ui.component.pensionBallColors
import junjange.feature.mynumber.MyNumberContract.Effect.*
import junjange.feature.mynumber.MyNumberContract.Event.*
import junjange.feature.mynumber.MyNumberContract.PagedContent
import junjange.feature.mynumber.MyNumberContract.PageLoadState
import junjange.feature.mynumber.MyNumberContract.UserRoundId
import junjange.feature.mynumber.dialog.LotteryDeleteDialog
import junjange.feature.mynumber.resources.Res
import junjange.feature.mynumber.resources.delete
import junjange.feature.mynumber.resources.delete_close
import junjange.feature.mynumber.resources.delete_count
import junjange.feature.mynumber.resources.delete_failed
import junjange.feature.mynumber.resources.delete_select_title
import junjange.feature.mynumber.resources.delete_selected_count
import junjange.feature.mynumber.resources.delete_success
import junjange.feature.mynumber.resources.deselect_all
import junjange.feature.mynumber.resources.empty_lotto_description
import junjange.feature.mynumber.resources.empty_lotto_title
import junjange.feature.mynumber.resources.empty_pension_lotto_description
import junjange.feature.mynumber.resources.empty_pension_lotto_title
import junjange.feature.mynumber.resources.group_title
import junjange.feature.mynumber.resources.my_numbers_title
import junjange.feature.mynumber.resources.winning_numbers_title
import junjange.feature.mynumber.resources.lottery_insert_failed
import junjange.feature.mynumber.resources.lotto_645_title
import junjange.feature.mynumber.resources.lotto_720_title
import junjange.feature.mynumber.resources.lotto_number_submitted
import junjange.feature.mynumber.resources.pension_lottery_insert_failed
import junjange.feature.mynumber.resources.pension_lottery_number_submitted
import junjange.feature.mynumber.resources.select_all
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

/**
 * @param onChromeHidden 하단 탭 바를 감출지 셸에 알린다. 하단 내비게이션은 Android는 상위 Scaffold,
 *   iOS는 SwiftUI TabView가 들고 있어 이 화면이 직접 치울 수 없다. 삭제 모드에서는 하단이
 *   삭제 버튼 하나만 남아야 하고, 번호를 담는 화면은 전체 화면이라 탭 바가 함께 사라져야 한다.
 */
// BackHandler는 실험 API이고 NavigationEventHandler로 대체될 예정이지만, 이 버전에는 아직
// 대체 API가 들어와 있지 않다. 교체 시점에 이 OptIn을 함께 지운다.
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyNumberScreen(
    viewModel: MyNumberViewModel,
    initialPage: Int,
    onChromeHidden: (Boolean) -> Unit = {},
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

    // 번호를 담는 전체 화면을 띄울 복권 종류. null이면 목록을 보여준다.
    var entryType by remember { mutableStateOf<LottoType?>(null) }

    val deleteLottery = remember { mutableStateListOf<UserRoundId>() }
    val deletePensionLottery = remember { mutableStateListOf<UserRoundId>() }

    val snackbarHostState = remember { SnackbarHostState() }

    val isChromeHidden = isDeleteMode || entryType != null

    LaunchedEffect(isChromeHidden) { onChromeHidden(isChromeHidden) }

    // 삭제 모드에서 뒤로 가면 모드만 벗어난다. 걸어두지 않으면 Android에서 앱이 그대로 종료된다.
    // 번호를 담는 화면은 저장하지 않은 것을 되물어야 해서 그 화면이 직접 뒤로 가기를 받는다.
    BackHandler(enabled = isDeleteMode) {
        isDeleteMode = false
        isAllSelected = false
        deleteLottery.clear()
        deletePensionLottery.clear()
    }

    // 이 상태로 화면을 벗어나면 탭 바가 사라진 채로 남는다.
    DisposableEffect(Unit) {
        onDispose { onChromeHidden(false) }
    }

    // 화면이 다시 보일 때마다 갱신한다.
    //
    // LaunchedEffect(Unit)로는 첫 컴포지션에서 한 번만 돌았다. iOS에서 탭 콘텐츠는 SwiftUI가
    // 살려 두므로, 랜덤 생성 화면에서 저장한 뒤 뒤로 가기로 나와 이 탭으로 돌아오면
    // 컴포지션이 그대로 남아 있어 목록이 갱신되지 않았다(당겨서 새로고침해야 보였다).
    // 저장 후 스낵바의 '번호 확인'으로 올 때만 셸이 화면을 다시 만들어 갱신됐다.
    //
    // CMP의 Lifecycle은 뷰 컨트롤러의 viewWillAppear를 따르므로 탭이 다시 보이는 순간
    // RESUMED가 온다. Android도 같은 신호를 쓴다.
    LifecycleResumeEffect(Unit) {
        viewModel.event(RefreshLottery)
        viewModel.event(RefreshPensionLottery)
        onPauseOrDispose {}
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
                            MyNumberMessage.DELETE_SUCCESS -> getString(Res.string.delete_success)
                            MyNumberMessage.DELETE_FAILED -> getString(Res.string.delete_failed)
                        }
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    // 삭제 모드에서는 페이저 스와이프를 막아 두므로 선택은 항상 지금 보고 있는 탭의 것이다.
    val isLotteryPage = pagerState.currentPage == 0
    val selectedCount = if (isLotteryPage) deleteLottery.size else deletePensionLottery.size
    val endReached = if (isLotteryPage) state.lottery.endReached else state.pensionLottery.endReached

    if (state.isDeleteLotteryDialogShowing) {
        LotteryDeleteDialog(
            count = selectedCount,
            onDismiss = { viewModel.event(ShowDialog(isDialogShowing = false)) },
            okClick = {
                // '전체 선택'은 화면에 불러온 것만 체크한다. 마지막 페이지까지 다 불러온 뒤라면
                // 전체 삭제와 결과가 같아서 쿼리 한 번으로 끝내고, 아니라면 고른 것만 지운다.
                // 그러지 않으면 아직 못 본 회차까지 사라져 화면에 보인 개수와 결과가 어긋난다.
                val deleteAll = isAllSelected && endReached
                if (isLotteryPage) {
                    if (deleteAll) {
                        viewModel.event(DeleteAllLottery)
                    } else if (deleteLottery.isNotEmpty()) {
                        viewModel.event(DeleteLottery(deleteLottery.toList()))
                    }
                } else {
                    if (deleteAll) {
                        viewModel.event(DeleteAllPensionLottery)
                    } else if (deletePensionLottery.isNotEmpty()) {
                        viewModel.event(DeletePensionLottery(deletePensionLottery.toList()))
                    }
                }

                deleteLottery.clear()
                deletePensionLottery.clear()
                isAllSelected = false
                viewModel.event(ShowDialog(isDialogShowing = false))
                isDeleteMode = false
            },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MyNumberContent(
            tabs = tabs,
            pagerState = pagerState,
            lotteryContent = state.lottery,
            pensionLotteryContent = state.pensionLottery,
            deleteLottery = deleteLottery,
            deletePensionLottery = deletePensionLottery,
            isDeleteMode = isDeleteMode,
            selectedCount = selectedCount,
            snackbarHostState = snackbarHostState,
            onGalleryClicked = { viewModel.event(PickedImage) },
            onDeleteClicked = { deleteMode -> isDeleteMode = deleteMode },
            onEditClicked = {
                entryType = if (isLotteryPage) LottoType.LOTTO645 else LottoType.LOTTO720
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
            // 고른 게 없으면 삭제 버튼 자체가 비활성이므로 여기서 다시 걸러낼 필요가 없다.
            onDeleteLotteryClicked = { viewModel.event(ShowDialog(isDialogShowing = true)) },
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

        // 목록을 덮는다. 목록을 조건부로 빼버리면 스크롤 위치를 잃고, 닫고 돌아왔을 때 맨 위로 튄다.
        //
        // 복권 종류마다 따로 감싼다. 하나로 묶고 안에서 종류를 분기하면, 닫힐 때
        // entryType이 null이 되면서 내용이 먼저 비어 버려 내려가는 애니메이션에 아무것도 안 보인다.
        NumberEntryOverlay(visible = entryType == LottoType.LOTTO645) {
            LottoNumberEntry(
                onClose = { entryType = null },
                onSubmit = { lotteries ->
                    entryType = null
                    viewModel.event(InsertLotteries(lotteries))
                },
            )
        }

        NumberEntryOverlay(visible = entryType == LottoType.LOTTO720) {
            PensionLotteryNumberEntry(
                onClose = { entryType = null },
                onSubmit = { pensionLotteries ->
                    entryType = null
                    viewModel.event(InsertPensionLotteries(pensionLotteries))
                },
            )
        }
    }
}

/**
 * 번호 담기 화면을 목록 위에 띄우는 자리.
 *
 * 아래에서 올라오고 아래로 내려간다. 닫기(X)로 빠져나가는 화면이라 iOS의 모달 제시와 같은
 * 방향을 쓴다. 뒤로 가기(←)로 빠져나가는 화면은 오른쪽에서 들어오는 것과 구분된다.
 */
@Composable
private fun BoxScope.NumberEntryOverlay(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        // 불투명하게 덮어야 뒤 목록이 비쳐 보이지 않는다.
        Surface(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

/**
 * 복권 종류 선택.
 *
 * 밑줄 탭 대신 세그먼트 컨트롤을 쓴다. 종류가 둘뿐이고 서로 배타적인 선택이라
 * 두 칸이 한눈에 다 보이는 편이 지금 무엇을 보고 있는지 읽기 쉽다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LotteryTypeTabs(
    tabs: List<StringResource>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        modifier =
            Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .fillMaxWidth()
                .padding(
                    horizontal = LottoSpacing.screenHorizontal,
                    vertical = LottoSpacing.md,
                ),
    ) {
        tabs.forEachIndexed { index, title ->
            SegmentedButton(
                selected = selectedIndex == index,
                onClick = { onSelect(index) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = tabs.size),
                // 기본 체크 아이콘은 선택될 때만 나타나 라벨을 밀어내므로 쓰지 않는다.
                icon = {},
                label = {
                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                    )
                },
            )
        }
    }
}

/**
 * 삭제 모드 상단 바.
 *
 * 예전에는 '취소 / 전체 선택 / 삭제' 세 개가 글자 링크로만 붙어 있어서, 지금 몇 개를 고른 건지
 * 알 수 없고 되돌릴 수 없는 삭제가 손이 잘 닿는 곳도 아닌 맨 위 구석에 있었다.
 * 여기서는 고른 개수만 알려주고, 실행은 엄지가 닿는 하단 바로 내린다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteModeTopBar(
    selectedCount: Int,
    isAllSelected: Boolean,
    onSelectAllClicked: () -> Unit,
    onCancelClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text =
                    if (selectedCount == 0) {
                        stringResource(Res.string.delete_select_title)
                    } else {
                        stringResource(Res.string.delete_selected_count, selectedCount)
                    },
                maxLines = 1,
            )
        },
        navigationIcon = {
            IconButton(onClick = onCancelClicked) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(Res.string.delete_close),
                )
            }
        },
        actions = {
            TextButton(onClick = onSelectAllClicked) {
                Text(
                    text =
                        stringResource(
                            if (isAllSelected) Res.string.deselect_all else Res.string.select_all,
                        ),
                )
            }
        },
    )
}

/**
 * 삭제 모드 하단 바. 고른 개수를 버튼 글자에 넣어 몇 개가 사라지는지 누르기 전에 보이게 한다.
 *
 * 하나도 고르지 않았으면 비활성으로 둔다. 예전에는 눌리긴 하고 '삭제할 번호가 없어요' 스낵바가
 * 떴는데, 누를 수 있는 버튼이 거절하는 것보다 아예 못 누르는 편이 덜 헷갈린다.
 */
@Composable
private fun DeleteModeBottomBar(
    selectedCount: Int,
    onDeleteClicked: () -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Button(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = LottoSpacing.screenHorizontal,
                        vertical = LottoSpacing.md,
                    ).height(52.dp),
            onClick = onDeleteClicked,
            enabled = selectedCount > 0,
            shape = LottoShapeTokens.button,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
        ) {
            Text(
                text =
                    if (selectedCount == 0) {
                        stringResource(Res.string.delete)
                    } else {
                        stringResource(Res.string.delete_count, selectedCount)
                    },
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
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
    selectedCount: Int,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: (isDeleteMode: Boolean) -> Unit,
    onEditClicked: () -> Unit,
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        // 위는 각 상단 바가, 아래는 상위 Scaffold의 내비게이션 바(iOS는 셸의 safe area)가
        // 이미 시스템 영역을 비워 둔다. 여기서 또 넣으면 목록 아래에 빈 띠가 생긴다.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (isDeleteMode) {
                DeleteModeTopBar(
                    selectedCount = selectedCount,
                    isAllSelected = isAllSelected,
                    onSelectAllClicked = onSelectAllClicked,
                    onCancelClicked = onDeleteLotteryCancelClicked,
                )
            } else {
                LotteryTypeTabs(
                    tabs = tabs,
                    selectedIndex = pagerState.currentPage,
                    onSelect = { index -> coroutineScope.launch { pagerState.scrollToPage(index) } },
                )
            }
        },
        bottomBar = {
            if (isDeleteMode) {
                DeleteModeBottomBar(
                    selectedCount = selectedCount,
                    onDeleteClicked = onDeleteLotteryClicked,
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
                            onEditClicked = onEditClicked,
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
                            onEditClicked = onEditClicked,
                            onGalleryClicked = onGalleryClicked,
                            onDeleteClicked = { onDeleteClicked(true) },
                            onRefresh = onRefreshPensionLottery,
                            onLoadMore = onLoadMorePensionLottery,
                            checkedPensionLottery = checkedPensionLottery,
                        )
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
                    val lotteryGetContent = contents.items[it]

                    LotteryCard(
                        modifier =
                            Modifier.padding(
                                horizontal = LottoSpacing.screenHorizontal,
                                vertical = LottoSpacing.sm,
                            ),
                        title = stringResource(Res.string.lotto_645_title),
                        subtitle =
                            lotteryRoundSubtitle(
                                round = lotteryGetContent.round,
                                winningDate = lotteryGetContent.winningDate,
                            ),
                    ) {
                        lotteryGetContent.winningLotteryNumbers?.let { winning ->
                            Lotto645WinningSection(
                                numbers =
                                    listOf(
                                        winning.firstNum,
                                        winning.secondNum,
                                        winning.thirdNum,
                                        winning.fourthNum,
                                        winning.fifthNum,
                                        winning.sixthNum,
                                    ),
                                bonus = winning.bonusNum,
                                size = SavedBallSize,
                            )
                            Spacer(modifier = Modifier.height(LottoSpacing.base))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(LottoSpacing.base))
                        }

                        MyLotteryNumber(
                            round = lotteryGetContent.round,
                            lotteryGetNumbers = lotteryGetContent.lotteryGetNumbers,
                            isDeleteMode = isDeleteMode,
                            deleteLottery = deleteLottery,
                            checkedLottery = checkedLottery,
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        if (!isDeleteMode) {
            NumberActionFab(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = LottoSpacing.base, end = LottoSpacing.base),
                onEditClicked = onEditClicked,
                onGalleryClicked = onGalleryClicked,
                expanded = firstVisibleItemScrollOffset.value == 0,
                deleteEnabled = contents.items.isNotEmpty(),
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
    SavedNumberList(label = stringResource(Res.string.my_numbers_title)) {
        lotteryGetNumbers.forEach { lotteryGetNumber ->
            val userRoundId = UserRoundId(round = round, id = lotteryGetNumber.id)

            SavedNumberRow(
                rank = lotteryGetNumber.rank,
                isDeleteMode = isDeleteMode,
                checked = deleteLottery.contains(userRoundId),
                onCheckedChange = { checkedLottery(userRoundId) },
            ) {
                val numbers =
                    listOf(
                        lotteryGetNumber.firstNum,
                        lotteryGetNumber.secondNum,
                        lotteryGetNumber.thirdNum,
                        lotteryGetNumber.fourthNum,
                        lotteryGetNumber.fifthNum,
                        lotteryGetNumber.sixthNum,
                    )
                // 볼 여섯 개는 한 덩어리로 묶어 안쪽 간격을 따로 준다.
                // 360dp 기준 폭: 등수 52 + 행 간격 4 + (볼 30×6 + 간격 8×5) = 276dp ≤ 카드 안쪽 280dp.
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LottoSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    numbers.forEachIndexed { index, number ->
                        MyLotteryBall(
                            isSuccess = lotteryGetNumber.correctNumbers?.getOrNull(index) == true,
                            lottoTitle = number.toString(),
                            color = number.toLotteryColor(),
                        )
                    }
                }
            }
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
                    val pensionLotteryGetContent = contents.items[it]

                    LotteryCard(
                        modifier =
                            Modifier.padding(
                                horizontal = LottoSpacing.screenHorizontal,
                                vertical = LottoSpacing.sm,
                            ),
                        title = stringResource(Res.string.lotto_720_title),
                        subtitle =
                            lotteryRoundSubtitle(
                                round = pensionLotteryGetContent.round,
                                winningDate = pensionLotteryGetContent.winningDate,
                            ),
                    ) {
                        pensionLotteryGetContent.winningPensionLotteryNumbers?.let { winning ->
                            LottoNumberSection(label = stringResource(Res.string.winning_numbers_title)) {
                                LottoPensionBalls(
                                    group = winning.group.toString(),
                                    numbers =
                                        listOf(
                                            winning.firstNum,
                                            winning.secondNum,
                                            winning.thirdNum,
                                            winning.fourthNum,
                                            winning.fifthNum,
                                            winning.sixthNum,
                                        ).map { num -> num.toString() },
                                    size = SavedPensionBallSize,
                                )
                            }
                            Spacer(modifier = Modifier.height(LottoSpacing.base))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(LottoSpacing.base))
                        }

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

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        if (!isDeleteMode) {
            NumberActionFab(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = LottoSpacing.base, end = LottoSpacing.base),
                onEditClicked = onEditClicked,
                onGalleryClicked = onGalleryClicked,
                deleteEnabled = contents.items.isNotEmpty(),
                onDeleteClicked = onDeleteClicked,
                expanded = firstVisibleItemScrollOffset.value == 0,
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
    SavedNumberList(label = stringResource(Res.string.my_numbers_title)) {
        pensionLotteryNumbers.forEach { pensionLotteryNumber ->
            val userRounds = UserRoundId(round = round, id = pensionLotteryNumber.id)

            SavedNumberRow(
                rank = pensionLotteryNumber.rank,
                isDeleteMode = isDeleteMode,
                checked = deletePensionLottery.contains(userRounds),
                onCheckedChange = { checkedPensionLottery(userRounds) },
            ) {
                LottoGroupChip(
                    group = pensionLotteryNumber.group.toString(),
                    height = SavedPensionBallSize,
                )
                val numbers =
                    listOf(
                        pensionLotteryNumber.firstNum,
                        pensionLotteryNumber.secondNum,
                        pensionLotteryNumber.thirdNum,
                        pensionLotteryNumber.fourthNum,
                        pensionLotteryNumber.fifthNum,
                        pensionLotteryNumber.sixthNum,
                    ).map { it.toString() }

                // 360dp 기준 폭: 등수 52 + 4 + 조 칩 40 + 4 + (볼 26×6 + 간격 4×5) = 276dp.
                // 조 칩까지 들어가는 행이라 앱에서 가장 빠듯하다. 6/45 행의 절반인 4dp가 상한이다.
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LottoSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    numbers.forEachIndexed { index, title ->
                        // 조가 맞아야 번호도 의미가 있어서, 자리 인덱스는 조를 뺀 값으로 맞춘다.
                        MyPensionLotteryBall(
                            isSuccess =
                                checkWinningBonus ||
                                    pensionLotteryNumber.correctNumbers?.getOrNull(index + 1) == true,
                            lottoTitle = title,
                            index = index,
                        )
                    }
                }
            }
        }
    }
}

/**
 * 저장한 번호 묶음. 라벨을 한 번만 두고 행을 쌓는다.
 *
 * 행 사이를 살짝 띄운다. 붙여 두면 삭제 모드에서 연달아 고른 줄들의 배경이 한 덩어리로 뭉쳐
 * 몇 줄을 골랐는지 세기 어렵다.
 */
@Composable
private fun SavedNumberList(
    label: String,
    rows: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(LottoSpacing.xxs)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(LottoSpacing.sm))
        rows()
    }
}

/**
 * 저장한 번호 한 줄. 왼쪽에 등수, 오른쪽에 번호.
 *
 * 셀마다 테두리를 둘러 표를 그리면 맞닿는 변이 두 겹이 되어 스프레드시트처럼 보인다.
 * 테두리를 없애고 등수 폭만 고정해서 행끼리 번호가 세로로 맞도록 한다.
 *
 * 삭제 모드에서는 행 전체가 하나의 선택 대상이 된다. 체크박스만 누를 수 있게 두면 24dp짜리
 * 과녁을 여러 번 겨눠야 하고, 번호를 보고 고르는 동작과 누르는 자리가 따로 놀게 된다.
 */
@Composable
private fun SavedNumberRow(
    rank: String?,
    isDeleteMode: Boolean,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    balls: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier =
            // 잘라 두지 않으면 리플이 둥근 모서리를 넘어 각진 사각형으로 번진다.
            Modifier.fillMaxWidth().clip(LottoShapes.small).then(
                if (isDeleteMode) {
                    Modifier.toggleable(
                        value = checked,
                        role = Role.Checkbox,
                        onValueChange = { onCheckedChange() },
                    )
                } else {
                    Modifier
                },
            ),
        shape = LottoShapes.small,
        // 고른 줄은 배경으로 표시한다. 지울 대상이라 빨강이 맞아 보이지만, 여러 줄이 붉게 물들면
        // 목록 전체가 경고처럼 읽힌다. 되돌릴 수 없다는 신호는 하단 버튼 하나로 충분하다.
        color =
            if (isDeleteMode && checked) {
                MaterialTheme.colorScheme.surfaceContainerHigh
            } else {
                Color.Transparent
            },
    ) {
        Row(
            modifier = Modifier.height(44.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LottoSpacing.xs),
        ) {
            Box(
                modifier = Modifier.width(RankSlotWidth),
                contentAlignment = Alignment.Center,
            ) {
                if (isDeleteMode) {
                    // 행이 토글을 맡으므로 체크박스는 상태만 보여준다. 둘 다 누를 수 있으면
                    // 체크박스 위에서만 리플이 따로 튀고 접근성 노드도 두 개가 된다.
                    Checkbox(
                        checked = checked,
                        onCheckedChange = null,
                    )
                } else {
                    RankBadge(rank = rank)
                }
            }
            balls()
        }
    }
}

/** 당첨으로 볼 등수 코드. 이 목록에 없으면 꽝이거나 아직 추첨 전이다. */
private val WinningRanks =
    setOf("FIRST", "SECOND", "THIRD", "FOURTH", "FIFTH", "SIXTH", "SEVENTH")

/**
 * 등수 배지. 당첨된 건만 브랜드 색으로 올리고 나머지는 뉴트럴로 눕힌다.
 *
 * 'NONE만 아니면 당첨'으로 두면 추첨 전(미발표)까지 당첨으로 칠해진다. 등수 코드를 직접 확인한다.
 */
@Composable
private fun RankBadge(rank: String?) {
    val isWinning = rank in WinningRanks
    Surface(
        shape = LottoShapes.small,
        color =
            if (isWinning) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
        contentColor =
            if (isWinning) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
    ) {
        Text(
            modifier =
                Modifier.padding(
                    horizontal = LottoSpacing.sm,
                    vertical = LottoSpacing.xs,
                ),
            text = rank.toRankTitle(),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * 등수 배지 자리 폭.
 *
 * "미발표"가 가장 긴 값이다. labelMedium(12sp) 세 글자 36dp에 배지 좌우 여백 8dp씩을 더한 52dp가
 * 정확한 크기다. 삭제 모드에 들어가는 체크박스(48dp)도 이 안에 들어간다.
 * 예전 56dp는 4dp가 남는 자리였고, 그만큼 오른쪽 볼 사이 간격을 못 벌리고 있었다.
 */
private val RankSlotWidth = 52.dp

/** 저장한 6/45 번호 볼 크기. 두 자리 숫자가 들어가므로 연금복권보다 크다. */
private val SavedBallSize = 30.dp

/** 저장한 연금복권 볼 크기. 한 행에 조 칩 + 볼 여섯 개가 들어가야 해서 더 작다. */
private val SavedPensionBallSize = 26.dp

/**
 * 저장한 6/45 번호 한 알.
 *
 * 맞은 번호만 실제 색으로 채우고 나머지는 뉴트럴로 눕힌다. 어느 번호가 맞았는지가
 * 이 화면에서 가장 먼저 읽혀야 하는 정보이므로 색을 그 신호로만 쓴다.
 */
@Composable
fun MyLotteryBall(
    isSuccess: Boolean,
    lottoTitle: String,
    color: Color,
) {
    Surface(
        modifier = Modifier.size(SavedBallSize),
        shape = CircleShape,
        color = if (isSuccess) color else MaterialTheme.colorScheme.surfaceContainer,
        contentColor = if (isSuccess) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = lottoTitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

/** 저장한 연금복권 번호 한 알. 실물이 흰 볼에 색 테두리라 맞은 자리만 테두리를 준다. */
@Composable
fun MyPensionLotteryBall(
    isSuccess: Boolean,
    lottoTitle: String,
    index: Int,
) {
    Surface(
        modifier = Modifier.size(SavedPensionBallSize),
        border = if (isSuccess) BorderStroke(3.dp, pensionBallColors[index]) else null,
        shape = CircleShape,
        color =
            if (isSuccess) {
                MaterialTheme.colorScheme.surfaceContainerLowest
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
        contentColor =
            if (isSuccess) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = lottoTitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
