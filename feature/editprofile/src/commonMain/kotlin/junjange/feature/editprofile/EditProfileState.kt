package junjange.feature.editprofile

data class EditProfileState(
    val isLoading: Boolean = false,
    val isNotificationAvailable: Boolean = false,
    val isEditMode: Boolean = false,
    val newNickname: String = "",
    val currentNickName: String = "",
    val profileImageFile: Any? = null, // TODO: Replace with multiplatform file abstraction
    val currentProfileImage: String? = null,
    val newProfileImage: String? = null,
    val newProfileImageBitmap: Any? = null, // TODO: Replace with multiplatform image abstraction
    val isBottomSheetShowing: Boolean = false,
)
