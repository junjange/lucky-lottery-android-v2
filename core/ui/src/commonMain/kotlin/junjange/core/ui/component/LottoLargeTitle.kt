package junjange.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoSpacing
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * 탭 루트 화면의 큰 제목.
 *
 * 떠 있는 앱바 대신 콘텐츠와 같은 배경에 큰 제목을 얹는다.
 * 좌우 여백은 아래 카드와 같은 [LottoSpacing.screenHorizontal]을 써서 왼쪽 선이 맞도록 한다.
 *
 * 뒤로 가기나 닫기가 있는 화면에는 쓰지 않는다. 그쪽은 M3 `TopAppBar`가 맡는다.
 * 루트는 큰 제목, 그 아래로 들어간 화면은 일반 앱바 — iOS가 라지 타이틀을 쓰는 규칙과 같다.
 *
 * M3 `LargeTopAppBar`를 쓰지 않는 이유는 그쪽이 제목 위에 뒤로·액션이 들어갈 64dp 줄을 늘
 * 비워 두기 때문이다. 뒤로도 액션도 없는 루트에서는 그 줄이 통째로 빈 자리가 된다.
 *
 * @param actionIconRes 제목 오른쪽에 붙일 아이콘. 없으면 제목만 놓인다.
 */
@Composable
fun LottoLargeTitle(
    title: String,
    modifier: Modifier = Modifier,
    actionIconRes: DrawableResource? = null,
    actionDescription: String? = null,
    onActionClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(
                    start = LottoSpacing.screenHorizontal,
                    end = LottoSpacing.sm,
                    top = LottoSpacing.xl,
                    bottom = LottoSpacing.base,
                ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        actionIconRes?.let {
            IconButton(onClick = onActionClick) {
                Icon(
                    painter = painterResource(it),
                    contentDescription = actionDescription,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
