package junjange.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import junjange.core.designsystem.theme.LottoTheme.typography

@Composable
fun LottoTheme(
    colors: LottoColors = LottoTheme.colors,
    content: @Composable () -> Unit,
) {
    val rememberedColors = remember { colors.copy() }

    PlatformThemeEffect()

    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            content = content,
        )
    }
}

@Composable
expect fun PlatformThemeEffect()

object LottoTheme {
    val colors: LottoColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: LottoTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
