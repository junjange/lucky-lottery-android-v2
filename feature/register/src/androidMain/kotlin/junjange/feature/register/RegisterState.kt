package junjange.feature.register

import android.graphics.Bitmap

data class RegisterState(
    val isLoading: Boolean = false,
    val newNickname: String = "",
    val newProfileImage: Bitmap? = null,
    val isBottomSheetShowing: Boolean = false,
)
