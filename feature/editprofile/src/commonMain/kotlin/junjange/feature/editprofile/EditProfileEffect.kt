package junjange.feature.editprofile

sealed class EditProfileEffect {
    data object LaunchImagePicker : EditProfileEffect()

    data object ProfileUpdateSuccess : EditProfileEffect()
}
