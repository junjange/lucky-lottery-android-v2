package junjange.feature.splash

sealed interface SplashContract {
    sealed interface Effect {
        data object AlreadyLoggedIn : Effect

        data object RequireLoginIn : Effect
    }
}
