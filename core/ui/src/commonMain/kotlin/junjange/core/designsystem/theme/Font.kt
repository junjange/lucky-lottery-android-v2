package junjange.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import junjange.core.ui.resources.Res
import junjange.core.ui.resources.pretendard_bold
import junjange.core.ui.resources.pretendard_medium
import junjange.core.ui.resources.pretendard_regular
import junjange.core.ui.resources.pretendard_semibold
import org.jetbrains.compose.resources.Font

/**
 * 두 플랫폼에서 동일한 타이포를 쓰기 위해 번들한 본문 서체.
 * 네 가지 weight만 번들하므로 타이포 스케일도 400/500/600/700 안에서만 정의한다.
 */
@Composable
fun pretendardFontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.pretendard_regular, FontWeight.Normal),
        Font(Res.font.pretendard_medium, FontWeight.Medium),
        Font(Res.font.pretendard_semibold, FontWeight.SemiBold),
        Font(Res.font.pretendard_bold, FontWeight.Bold),
    )
