package junjange.feature.register

import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.component.EditProfileType
import junjange.core.ui.component.LottoButtonTopBar
import junjange.core.ui.component.LottoEditProfileBottomSheet
import junjange.core.ui.component.LottoProfileTextField
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigateToMain: () -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val imageCropLauncher =
        rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent?.let {
                    val bitmap =
                        if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, it)
                            ImageDecoder.decodeBitmap(source)
                        }
                    viewModel.onPickImage(bitmap)
                }
            }
        }

    val imageCropperOptions =
        CropImageOptions(
            fixAspectRatio = true,
            aspectRatioX = 1,
            aspectRatioY = 1,
            toolbarColor = Color.WHITE,
            toolbarBackButtonColor = Color.BLACK,
            toolbarTintColor = Color.BLACK,
            allowFlipping = false,
            allowRotation = false,
            cropMenuCropButtonTitle = context.getString(R.string.done),
            imageSourceIncludeCamera = false,
        )

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val cropOptions = CropImageContractOptions(it, imageCropperOptions)
                imageCropLauncher.launch(cropOptions)
            }
        }

    val sheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        )

    LaunchedEffect(uiState.isBottomSheetShowing) {
        if (uiState.isBottomSheetShowing) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) viewModel.setBottomSheetShowing(isBottomSheetShowing = false)
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is RegisterEffect.LaunchImagePicker -> imagePickerLauncher.launch("image/*")
                is RegisterEffect.NavigateToMain -> navigateToMain()
                is RegisterEffect.Back -> onBack()
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            LottoEditProfileBottomSheet { editProfileType ->
                when (editProfileType) {
                    is EditProfileType.ProfileImageSelect -> viewModel.onClickedProfileImgSelect()
                    is EditProfileType.ProfileDefaultImageSelect -> viewModel.onClickedProfileDefaultImageSelect()
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                LottoButtonTopBar(
                    onBack = { viewModel.onClickedBack() },
                    titleRes = R.string.register,
                    buttonTextRes = R.string.done,
                    onClickButton = {
                        val deviceId = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
                        viewModel.onClickedRegister(deviceId = deviceId)
                    },
                    isEnabled = uiState.newNickname.isNotEmpty(),
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.padding(top = 114.dp)) {
                    // TODO 임시 데이터
                    AsyncImage(
                        model = "https://www.ikbc.co.kr/data/kbc/image/2023/08/13/kbc202308130007.800x.0.jpg",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(32.dp)),
                    )
                    uiState.newProfileImage?.let {
                        Image(
                            it.asImageBitmap(),
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(32.dp)),
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_camera),
                        contentDescription = null,
                        modifier =
                            Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 4.dp, y = 4.dp)
                                .clickable { viewModel.setBottomSheetShowing(isBottomSheetShowing = true) },
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))
                LottoProfileTextField(
                    value = uiState.newNickname,
                    onValueChange = viewModel::inputNickname,
                    onClear = { viewModel.inputNickname("") },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.register_help),
                    style = LottoTheme.typography.caption2,
                    color = LottoTheme.colors.gray600,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
