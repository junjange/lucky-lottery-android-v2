package junjange.feature.mynumber.dialog

import androidx.compose.runtime.Composable
import junjange.core.designsystem.components.dialog.LottoTwoButtonDialog
import junjange.feature.mynumber.resources.Res
import junjange.feature.mynumber.resources.delete_cancel
import junjange.feature.mynumber.resources.dialog_delete_confirm
import junjange.feature.mynumber.resources.dialog_delete_message
import junjange.feature.mynumber.resources.dialog_delete_title
import org.jetbrains.compose.resources.stringResource

/**
 * 삭제 확인. 디자인 시스템의 두 갈래 대화상자를 쓴다.
 *
 * 예전에는 312x200dp로 크기를 못 박은 커스텀 Surface였고, 그 다음에는 M3 기본 `AlertDialog`였다.
 * 둘 다 이 화면만의 대화상자여서 같은 앱 안에서 알림 설정 대화상자와 모양이 달랐다.
 * 대화상자는 [LottoTwoButtonDialog] 하나로 모은다.
 *
 * 확인 버튼은 빨강이 아니라 브랜드 색을 그대로 쓴다. 무엇이 사라지는지는 제목과 본문이
 * 이미 말하고 있고, 앱 안의 다른 확인 버튼과 색이 달라지면 그쪽이 더 낯설다.
 *
 * @param count 지울 번호 개수. 몇 개가 사라지는지 확인 순간에 다시 보여준다.
 */
@Composable
fun LotteryDeleteDialog(
    count: Int,
    onDismiss: () -> Unit,
    okClick: () -> Unit,
) {
    LottoTwoButtonDialog(
        title = stringResource(Res.string.dialog_delete_title, count),
        content = stringResource(Res.string.dialog_delete_message),
        confirmText = stringResource(Res.string.dialog_delete_confirm),
        cancelText = stringResource(Res.string.delete_cancel),
        onConfirm = okClick,
        onCancel = onDismiss,
    )
}
