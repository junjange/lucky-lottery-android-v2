package junjange.feature.splash

sealed interface SplashContract {
    sealed interface Effect {
        data object NavigateToMain : Effect
    }
}
