package junjange.featrue.register

import android.graphics.Bitmap

data class RegisterState(
    val isLoading: Boolean = false,
    val newNickname: String = "",
    val newProfileImage: Bitmap? = null,
    val isBottomSheetShowing: Boolean = false,
)

sealed class RegisterEffect {
    data object LaunchImagePicker : RegisterEffect()

    data object NavigateToMain : RegisterEffect()

    data object Back : RegisterEffect()
}
