package junjange.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.components.dialog.LottoTwoButtonDialog
import junjange.core.designsystem.theme.LottoShapeTokens
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.ui.resources.Res
import junjange.core.ui.resources.close
import junjange.core.ui.resources.entry_discard_cancel
import junjange.core.ui.resources.entry_discard_confirm
import junjange.core.ui.resources.entry_discard_message
import junjange.core.ui.resources.entry_discard_title
import org.jetbrains.compose.resources.stringResource

/** 하단 고정 버튼 높이. 화면 폭을 다 쓰는 주 동작이므로 목록 안 버튼보다 크게 둔다. */
private val CtaHeight = 56.dp

/**
 * 번호를 골라 담는 화면의 공통 틀.
 *
 * 예전에는 바텀시트에 입력 칸과 저장 버튼을 함께 넣었는데, 격자를 놓기에는 시트가 좁았다.
 * 시트가 화면을 거의 다 채우고, 그러면 저장 버튼이 스크롤 아래로 밀려 다 고른 뒤에
 * 화면을 내려야 저장할 수 있었다.
 *
 * 동작을 두 곳으로 나눈다. 하단 고정 버튼은 고른 번호를 목록에 **담는** 반복 동작이고,
 * 상단 오른쪽은 담은 것을 모두 **생성**하고 화면을 닫는 마지막 동작이다.
 * 예전에는 담기 버튼이 본문 흐름 안에 있어서 여섯 개를 채우는 순간 나타나며 아래 격자를
 * 밀어냈는데, 두 버튼을 다 화면 고정 자리로 빼면 무엇을 고르든 레이아웃이 움직이지 않는다.
 *
 * 하단 탭 바는 이 화면이 뜬 동안 셸이 내려주므로(Android는 상위 Scaffold,
 * iOS는 SwiftUI `TabView`) 아래에 버튼 하나만 남는다.
 *
 * @param addLabel 하단 담기 버튼 문구.
 * @param confirmLabel 상단 오른쪽 생성 버튼 문구.
 * @param hasUnsaved 아직 저장하지 않은 번호가 있는지. 있으면 닫기·뒤로 가기에서 먼저 되묻는다.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun NumberEntryScaffold(
    title: String,
    addLabel: String,
    addEnabled: Boolean,
    confirmLabel: String,
    confirmEnabled: Boolean,
    hasUnsaved: Boolean,
    onClose: () -> Unit,
    onAdd: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    var isDiscardDialogShowing by remember { mutableStateOf(false) }

    // 담은 것이 있는데 그냥 닫으면 아무 말 없이 사라진다. 저장은 '생성'을 눌러야 일어나므로
    // 닫기와 저장의 결과가 정반대인데, 그 차이를 알려주지 않으면 실수를 되돌릴 방법이 없다.
    fun requestClose() {
        if (hasUnsaved) isDiscardDialogShowing = true else onClose()
    }

    // 이 화면이 떠 있는 동안에는 시스템 뒤로 가기도 닫기와 같게 다룬다. 이 핸들러가 상위
    // 화면의 것보다 안쪽에 있으므로 여기서 먼저 소비된다.
    // iOS에서는 활성 핸들러가 있으면 왼쪽 엣지 스와이프가 여기로 들어온다.
    BackHandler { requestClose() }

    if (isDiscardDialogShowing) {
        LottoTwoButtonDialog(
            title = stringResource(Res.string.entry_discard_title),
            content = stringResource(Res.string.entry_discard_message),
            confirmText = stringResource(Res.string.entry_discard_confirm),
            cancelText = stringResource(Res.string.entry_discard_cancel),
            onConfirm = {
                isDiscardDialogShowing = false
                onClose()
            },
            onCancel = { isDiscardDialogShowing = false },
        )
    }

    Scaffold(
        // 상단은 TopAppBar가, 하단은 아래 Surface가 각자 시스템 영역을 비운다.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(text = title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { requestClose() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(Res.string.close),
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onConfirm, enabled = confirmEnabled) {
                        Text(text = confirmLabel, style = MaterialTheme.typography.titleMedium)
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
                            ).height(CtaHeight),
                    onClick = onAdd,
                    enabled = addEnabled,
                    shape = LottoShapeTokens.button,
                ) {
                    Text(text = addLabel, style = MaterialTheme.typography.titleMedium)
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = LottoSpacing.screenHorizontal),
            content = content,
        )
    }
}

/**
 * 번호를 누를 때의 촉각 피드백.
 *
 * 격자를 눌러도 키보드처럼 아무 반응이 없으면 눌린 것인지 화면을 봐야 확인된다.
 * `VirtualKey`는 Android에서 키보드 탭, iOS에서 가벼운 임팩트로 매핑되어 두 OS 모두
 * 자기 플랫폼의 기본 감촉을 낸다.
 */
@Composable
internal fun rememberNumberTapFeedback(): (Boolean) -> Unit {
    val haptics = LocalHapticFeedback.current
    return remember(haptics) {
        { isGameComplete ->
            haptics.performHapticFeedback(
                if (isGameComplete) HapticFeedbackType.Confirm else HapticFeedbackType.VirtualKey,
            )
        }
    }
}
