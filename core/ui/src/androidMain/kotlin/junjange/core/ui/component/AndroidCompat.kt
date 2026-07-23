@file:JvmName("AndroidCompatComponents")

package junjange.core.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Android-compatible wrapper that converts Android R.string/R.drawable Int IDs
 * to CMP resource types using stringResource/painterResource from androidMain.
 *
 * These are bridge functions for androidMain code that still uses R.string/R.drawable.
 */

// Convert Android string resource ID to CMP StringResource
// For androidMain code that needs to pass R.string.xxx to CMP components
@Composable
fun androidStringResource(@StringRes id: Int): String {
    return androidx.compose.ui.res.stringResource(id)
}

@Composable
fun androidPainterResource(@DrawableRes id: Int): androidx.compose.ui.graphics.painter.Painter {
    return androidx.compose.ui.res.painterResource(id)
}
