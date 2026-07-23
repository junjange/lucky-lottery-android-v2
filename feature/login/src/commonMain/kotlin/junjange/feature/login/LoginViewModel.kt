package junjange.feature.login

import junjange.core.domain.usecase.GetFCMTokenUseCase
import junjange.core.domain.usecase.GetValidRegisterUseCase
import junjange.core.domain.usecase.KakaoLoginUseCase
import junjange.core.domain.usecase.PostLoginUseCase
import junjange.core.domain.usecase.PostNotificationRegisterTokenUseCase
import junjange.core.domain.usecase.SaveJwtTokenUseCase
import junjange.core.ui.base.BaseViewModel
import junjange.feature.login.LoginEffect.NavigateToMain
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
        private val kakaoLoginUseCase: KakaoLoginUseCase,
        private val getValidRegisterUseCase: GetValidRegisterUseCase,
        private val postLoginUseCase: PostLoginUseCase,
        private val saveJwtTokenUseCase: SaveJwtTokenUseCase,
        private val postNotificationRegisterTokenUseCase: PostNotificationRegisterTokenUseCase,
        private val getFCMTokenUseCase: GetFCMTokenUseCase,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(LoginState())
        val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<LoginEffect>()
        val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

        fun kakaoLogin(deviceId: String) {
            launch {
                kakaoLoginUseCase()
                    .onSuccess {
                        it.idToken?.let { idToken ->
                            getValidRegisterUseCase(
                                idToken = idToken,
                                provider = KAKAO,
                            ).onSuccess { isRegistered ->
                                if (isRegistered.isRegistered) {
                                    postLogin(
                                        idToken = idToken,
                                        provider = KAKAO,
                                        deviceId = deviceId,
                                    )
                                } else {
                                    _effect.emit(
                                        LoginEffect.NavigateToRegister(
                                            idToken = idToken,
                                            provider = KAKAO,
                                        ),
                                    )
                                }
                            }.onFailure {
                                // TODO 예외 처리
                            }
                        }
                    }.onFailure {
                        // TODO 예외 처리
                    }
            }
        }

        fun googleLogin(
            idToken: String?,
            deviceId: String,
        ) {
            if (idToken == null) {
                // TODO 예외 처리 (sign-in cancelled or failed)
                return
            }
            launch {
                getValidRegisterUseCase(
                    idToken = idToken,
                    provider = GOOGLE,
                ).onSuccess { isRegistered ->
                    if (isRegistered.isRegistered) {
                        postLogin(
                            idToken = idToken,
                            provider = GOOGLE,
                            deviceId = deviceId,
                        )
                    } else {
                        _effect.emit(
                            LoginEffect.NavigateToRegister(
                                idToken = idToken,
                                provider = GOOGLE,
                            ),
                        )
                    }
                }.onFailure {
                    // TODO 예외 처리
                }
            }
        }

        private fun postLogin(
            idToken: String,
            provider: String,
            deviceId: String,
        ) {
            launch {
                postLoginUseCase(idToken = idToken, provider = provider)
                    .onSuccess {
                        saveJwtTokenUseCase(jwtToken = it)
                            .onSuccess {
                                getFCMToken(deviceId = deviceId)
                            }.onFailure {
                                // TODO 예외 처리
                            }
                    }.onFailure {
                        // TODO 예외 처리
                    }
            }
        }

        private fun getFCMToken(deviceId: String) {
            launch {
                getFCMTokenUseCase()
                    .onSuccess { fcmToken ->
                        postNotificationRegisterTokenUseCase(
                            deviceId = deviceId,
                            fcmToken = fcmToken,
                        ).onSuccess {
                            _effect.emit(NavigateToMain)
                        }.onFailure {
                            // TODO 예외 처리
                        }
                    }.onFailure {
                        // TODO 예외 처리
                    }
            }
        }

        companion object {
            const val KAKAO = "KAKAO"
            const val GOOGLE = "GOOGLE"
        }
    }
