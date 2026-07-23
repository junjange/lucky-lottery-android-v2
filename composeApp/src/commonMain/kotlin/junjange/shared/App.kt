package junjange.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.image.configureImageLoader

@Composable
fun App() {
    remember { configureImageLoader(); Unit }
    LottoTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SharedAppContent()
        }
    }
}

@Composable
expect fun SharedAppContent()
