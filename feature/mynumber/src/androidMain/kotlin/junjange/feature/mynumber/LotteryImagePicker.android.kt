package junjange.feature.mynumber

import android.graphics.Color
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import junjange.feature.mynumber.resources.Res
import junjange.feature.mynumber.resources.done
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun rememberLotteryImagePicker(onImagePicked: (imagePath: String) -> Unit): () -> Unit {
    val context = LocalContext.current
    val currentOnImagePicked by rememberUpdatedState(onImagePicked)
    val cropButtonTitle = stringResource(Res.string.done)

    val imageCropLauncher =
        rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent ?: return@rememberLauncherForActivityResult
                val imagePath =
                    result.getUriFilePath(context, false)
                        ?: return@rememberLauncherForActivityResult
                currentOnImagePicked(imagePath)
            }
        }

    val imageCropperOptions =
        CropImageOptions(
            cropShape = CropImageView.CropShape.RECTANGLE,
            fixAspectRatio = false,
            aspectRatioX = 1,
            aspectRatioY = 1,
            toolbarColor = Color.WHITE,
            toolbarBackButtonColor = Color.BLACK,
            toolbarTintColor = Color.BLACK,
            allowFlipping = false,
            allowRotation = false,
            cropMenuCropButtonTitle = cropButtonTitle,
            imageSourceIncludeCamera = false,
        )

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@rememberLauncherForActivityResult
            val cropOptions = CropImageContractOptions(uri, imageCropperOptions)
            imageCropLauncher.launch(cropOptions)
        }

    return { imagePickerLauncher.launch("image/*") }
}
