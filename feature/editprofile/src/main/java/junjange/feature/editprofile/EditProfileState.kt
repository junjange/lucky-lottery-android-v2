package junjange.feature.editprofile

import android.graphics.Bitmap
import java.io.File

data class EditProfileState(
    val isLoading: Boolean = false,
    val isNotificationAvailable: Boolean = false,
    val isEditMode: Boolean = false,
    val newNickname: String = "",
    val currentNickName: String = "",
    val profileImageFile: File? = null,
    val currentProfileImage: String? = null,
    val newProfileImage: String? = null,
    val newProfileImageBitmap: Bitmap? = null,
    val isBottomSheetShowing: Boolean = false,
)
