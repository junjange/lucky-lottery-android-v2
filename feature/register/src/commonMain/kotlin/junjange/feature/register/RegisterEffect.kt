package junjange.feature.register

sealed class RegisterEffect {
    data object LaunchImagePicker : RegisterEffect()

    data object NavigateToMain : RegisterEffect()

    data object Back : RegisterEffect()
}
