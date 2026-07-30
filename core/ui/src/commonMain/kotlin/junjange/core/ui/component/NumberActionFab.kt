package junjange.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import junjange.core.designsystem.theme.LottoSpacing
import junjange.core.ui.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 번호 관리 FAB. 누르면 액션을 바텀시트로 띄운다.
 *
 * 예전에는 FAB이 위로 펼쳐지는 스피드 다이얼이었는데, 그 방식은 스크림을 직접 그려야 하고
 * 그 스크림이 자기가 놓인 부모 크기만 덮는다. 상단 탭 바와 하단 내비게이션은 상위 Scaffold가
 * 들고 있어 절대 어두워지지 않고, iOS 탭 바는 SwiftUI라 Compose가 손댈 수 없다.
 * 바텀시트는 창 전체를 덮는 별도 레이어에 뜨므로 스크림이 한 번에 해결되고,
 * 펼침 애니메이션·바깥 탭 닫기·드래그 닫기도 OS 기본 동작을 그대로 쓴다.
 *
 * @param expanded 라벨을 함께 보여줄지. 스크롤을 내리면 아이콘만 남겨 목록을 가리지 않는다.
 */
@Composable
fun NumberActionFab(
    expanded: Boolean,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSheetOpen by remember { mutableStateOf(false) }

    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = { isSheetOpen = true },
        expanded = expanded,
        icon = {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(Res.string.addition),
            )
        },
        text = { Text(text = stringResource(Res.string.addition)) },
    )

    if (isSheetOpen) {
        NumberActionSheet(
            onDismissRequest = { isSheetOpen = false },
            onEditClicked = onEditClicked,
            onGalleryClicked = onGalleryClicked,
            onDeleteClicked = onDeleteClicked,
        )
    }
}

/**
 * 번호 관리 액션 목록.
 *
 * 항목을 고르면 시트를 닫는 애니메이션이 끝난 뒤에 동작을 실행한다. 바로 실행하면
 * 다음 화면(입력 시트·사진 선택기)이 닫히는 시트와 겹쳐 올라와 두 겹이 동시에 움직인다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NumberActionSheet(
    onDismissRequest: () -> Unit,
    onEditClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    fun select(action: () -> Unit) {
        scope.launch {
            sheetState.hide()
            onDismissRequest()
            action()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LottoSpacing.lg),
                text = stringResource(Res.string.addition),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(LottoSpacing.base))

            ActionRow(
                icon = painterResource(Res.drawable.baseline_edit_24),
                title = stringResource(Res.string.write_directly),
                onClick = { select(onEditClicked) },
            )
            ActionRow(
                icon = painterResource(Res.drawable.ic_photo),
                title = stringResource(Res.string.load_photo),
                onClick = { select(onGalleryClicked) },
            )

            // 삭제는 추가와 성격이 달라 선을 그어 떼어 놓고, 색으로 되돌릴 수 없는 동작임을 알린다.
            HorizontalDivider(
                modifier = Modifier.padding(vertical = LottoSpacing.sm),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            ActionRow(
                icon = painterResource(Res.drawable.ic_delete),
                title = stringResource(Res.string.delete_numbers),
                onClick = { select(onDeleteClicked) },
                tint = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(LottoSpacing.sm))
        }
    }
}

@Composable
private fun ActionRow(
    icon: Painter,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        leadingContent = {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = tint,
            )
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = tint,
            )
        },
    )
}
