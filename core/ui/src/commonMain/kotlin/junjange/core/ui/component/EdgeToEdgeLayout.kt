package junjange.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Edge-to-edge 레이아웃을 위한 래퍼 Composable
 * 시스템 바(상태 바, 네비게이션 바) 영역까지 컨텐츠를 확장하고
 * 필요한 경우 패딩을 자동으로 적용합니다.
 *
 * @param modifier 추가 modifier
 * @param applySystemBarsPadding 시스템 바 패딩 적용 여부 (기본값: true)
 * @param content 표시할 컨텐츠
 */
@Composable
fun EdgeToEdgeLayout(
    modifier: Modifier = Modifier,
    applySystemBarsPadding: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (applySystemBarsPadding) {
                    Modifier.windowInsetsPadding(WindowInsets.systemBars)
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}
