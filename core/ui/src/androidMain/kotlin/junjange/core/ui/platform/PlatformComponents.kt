@file:JvmName("PlatformComponentsAndroid")

package junjange.core.ui.platform

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import junjange.core.ui.component.AdmobBanner

@Composable
actual fun PlatformAdBanner(modifier: Modifier) {
    AdmobBanner(modifier = modifier)
}

@Composable
actual fun PlatformToastEffect(message: String?) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}
