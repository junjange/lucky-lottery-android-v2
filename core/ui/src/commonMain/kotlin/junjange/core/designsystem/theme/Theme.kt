package junjange.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun LottoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    PlatformThemeEffect(darkTheme)

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content,
    )
}

@Composable
expect fun PlatformThemeEffect(darkTheme: Boolean)

object LottoTheme {
    val typography: LottoTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
