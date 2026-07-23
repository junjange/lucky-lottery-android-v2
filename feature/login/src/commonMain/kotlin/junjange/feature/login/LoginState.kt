package junjange.feature.login

data class LoginState(
    val isLoading: Boolean = false,
)

sealed class LoginEffect {
    data object NavigateToMain : LoginEffect()

    class NavigateToRegister(val idToken: String, val provider: String) : LoginEffect()
}
