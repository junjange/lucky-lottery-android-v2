package com.junjange.presentation.ui.notification

import androidx.lifecycle.SavedStateHandle
import junjange.core.domain.usecase.PatchLotteryNotificationUseCase
import junjange.core.domain.usecase.PatchPensionLotteryNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import junjange.core.ui.base.BaseViewModel

@HiltViewModel
class NotificationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val patchLotteryNotificationUseCase: PatchLotteryNotificationUseCase,
        private val patchPensionLotteryNotificationUseCase: PatchPensionLotteryNotificationUseCase,
    ) : BaseViewModel() {
        private val isLottoNotificationAvailable =
            requireNotNull(
                savedStateHandle.get<Boolean>(NotificationActivity.EXTRA_KEY_LOTTO_NOTIFICATION_STATE),
            )

        private val isPensionLottoNotificationAvailable =
            requireNotNull(
                savedStateHandle.get<Boolean>(NotificationActivity.EXTRA_KEY_PENSION_LOTTO_NOTIFICATION_STATE),
            )

        private val _uiState = MutableStateFlow(NotificationState())
        val uiState: StateFlow<NotificationState> = _uiState.asStateFlow()

        init {
            _uiState.update { state ->
                state.copy(
                    isLottoNotificationAvailable = isLottoNotificationAvailable,
                    isPensionLottoNotificationAvailable = isPensionLottoNotificationAvailable,
                )
            }
        }

        fun setLottoNotification(enabled: Boolean) {
            launch {
                patchLotteryNotificationUseCase(enabled).onSuccess {
                    _uiState.update { it.copy(isLottoNotificationAvailable = enabled) }
                }.onFailure {
                    // TODO 예외처리
                }
            }
        }

        fun setPensionLottoNotification(enabled: Boolean) {
            launch {
                patchPensionLotteryNotificationUseCase(enabled).onSuccess {
                    _uiState.update { it.copy(isPensionLottoNotificationAvailable = enabled) }
                }.onFailure {
                    // TODO 예외처리
                }
            }
        }
    }
