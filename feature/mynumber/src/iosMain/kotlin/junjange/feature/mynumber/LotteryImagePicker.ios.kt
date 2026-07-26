package junjange.feature.mynumber

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import junjange.core.ui.platform.IosImagePickerBridge

@Composable
actual fun rememberLotteryImagePicker(onImagePicked: (imagePath: String) -> Unit): () -> Unit {
    val currentOnImagePicked by rememberUpdatedState(onImagePicked)
    return {
        IosImagePickerBridge.pickImage?.invoke { path ->
            path?.let { currentOnImagePicked(it) }
        }
    }
}
