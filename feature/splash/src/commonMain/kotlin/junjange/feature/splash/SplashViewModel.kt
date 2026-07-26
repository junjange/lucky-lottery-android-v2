package junjange.feature.splash

import junjange.core.ui.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow

class SplashViewModel : BaseViewModel() {
    private val _effect = Channel<SplashContract.Effect>(Channel.BUFFERED)
    val effect get() = _effect.receiveAsFlow()

    init {
        launch {
            delay(SPLASH_TIME)
            _effect.send(SplashContract.Effect.NavigateToMain)
        }
    }

    companion object {
        private const val SPLASH_TIME = 2000L
    }
}
