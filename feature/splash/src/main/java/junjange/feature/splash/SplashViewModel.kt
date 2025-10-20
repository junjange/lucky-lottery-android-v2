package junjange.feature.splash

import junjange.core.domain.usecase.GetJwtTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import junjange.core.ui.base.BaseViewModel

@HiltViewModel
class SplashViewModel
    @Inject
    constructor(
        private val getJwtTokenUseCase: GetJwtTokenUseCase,
    ) : BaseViewModel() {
        private val _effect = Channel<SplashContract.Effect>(Channel.BUFFERED)
        val effect get() = _effect.receiveAsFlow()

        init {
            launch {
                delay(SPLASH_TIME)
                _effect.send(SplashContract.Effect.AlreadyLoggedIn)
                // TODO 서버 로직 제거로 인해 주석
//                getJwtTokenUseCase().onSuccess {
//                    it?.let {
//                        _effect.send(SplashContract.Effect.AlreadyLoggedIn)
//                    } ?: run {
//                        _effect.send(SplashContract.Effect.RequireLoginIn)
//                    }
//                }
            }
        }

        companion object {
            private const val SPLASH_TIME = 2000L
        }
    }
