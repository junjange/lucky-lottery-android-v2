package com.junjange.presentation.ui.withdrawal

import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.junjange.presentation.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import junjange.core.domain.model.OauthProvider
import junjange.core.domain.usecase.DeleteLocalDataUseCase
import junjange.core.domain.usecase.DeleteMeUseCase
import junjange.core.domain.usecase.PostGoogleOauthTokenUseCase
import junjange.core.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val googleOauthTokenUseCase: PostGoogleOauthTokenUseCase,
        private val deleteMeUseCase: DeleteMeUseCase,
        private val deleteLocalDataUseCase: DeleteLocalDataUseCase,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(WithdrawalState())
        val uiState: StateFlow<WithdrawalState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<WithdrawalEffect>()
        val effect: SharedFlow<WithdrawalEffect> = _effect.asSharedFlow()

        init {
            initSavedState()
        }

        private fun initSavedState() {
            val oauthProvider =
                requireNotNull(
                    savedStateHandle.get<OauthProvider>(WithdrawalActivity.EXTRA_KEY_OAUTH_PROVIDER),
                )
            _uiState.update { state ->
                state.copy(oauthProvider = oauthProvider)
            }
        }

        fun addStep(step: Int) {
            _uiState.update { state ->
                state.copy(step = state.step + step)
            }
        }

        fun onClickedDialog(isWithdrawalDialogShowing: Boolean) {
            _uiState.update { state ->
                state.copy(isWithdrawalDialogShowing = isWithdrawalDialogShowing)
            }
        }

        fun googleLogin(result: Task<GoogleSignInAccount>?) {
            try {
                val account = result?.getResult(ApiException::class.java)
                account?.let {
                    account.serverAuthCode?.let { serverAuthCode ->
                        launch {
                            googleOauthTokenUseCase(
                                grantType = GRANT_TYPE,
                                clientId = BuildConfig.GOOGLE_CLIENT_ID,
                                clientSecret = BuildConfig.GOOGLE_CLIENT_SECRET,
                                redirectUri = REDIRECT_URI,
                                code = serverAuthCode,
                            ).onSuccess { googleOauthToken ->
                                deleteMe(oauthAccessToken = googleOauthToken.accessToken)
                            }.onFailure {
                                // TODO 예외 처리
                            }
                        }
                    } ?: run {
                        // TODO 예외 처리
                    }
                } ?: run {
                    // TODO 예외 처리
                }
            } catch (e: ApiException) {
                // TODO network error
            }
        }

        private fun deleteLocalData() {
            launch {
                deleteLocalDataUseCase()
                    .onSuccess {
                        _effect.emit(WithdrawalEffect.AddStep)
                    }.onFailure {
                        // TODO 예외 처리
                    }
            }
        }

        fun deleteMe(oauthAccessToken: String? = null) {
            launch {
                deleteMeUseCase(oauthAccessToken = oauthAccessToken)
                    .onSuccess {
                        deleteLocalData()
                    }.onFailure { }
            }
        }

        companion object {
            const val GRANT_TYPE = "authorization_code"
            const val REDIRECT_URI = ""
        }
    }
