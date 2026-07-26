package junjange.feature.notification

import junjange.core.domain.usecase.GetNotificationUseCase
import junjange.core.domain.usecase.PatchLotteryNotificationUseCase
import junjange.core.domain.usecase.PatchPensionLotteryNotificationUseCase
import junjange.core.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class NotificationViewModel
    
    constructor(
        private val getNotificationUseCase: GetNotificationUseCase,
        private val patchLotteryNotificationUseCase: PatchLotteryNotificationUseCase,
        private val patchPensionLotteryNotificationUseCase: PatchPensionLotteryNotificationUseCase,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(NotificationState())
        val uiState: StateFlow<NotificationState> = _uiState.asStateFlow()

        init {
            loadNotification()
        }

        private fun loadNotification() {
            launch {
                getNotificationUseCase().onSuccess { notification ->
                    _uiState.update { state ->
                        state.copy(
                            isLottoNotificationAvailable = notification.lotteryNotification,
                            isPensionLottoNotificationAvailable = notification.pensionLotteryNotification,
                        )
                    }
                }
            }
        }

        fun setLottoNotification(enabled: Boolean) {
            launch {
                patchLotteryNotificationUseCase(enabled)
                    .onSuccess {
                        _uiState.update { it.copy(isLottoNotificationAvailable = enabled) }
                    }.onFailure {
                        // TODO 예외처리
                    }
            }
        }

        fun setPensionLottoNotification(enabled: Boolean) {
            launch {
                patchPensionLotteryNotificationUseCase(enabled)
                    .onSuccess {
                        _uiState.update { it.copy(isPensionLottoNotificationAvailable = enabled) }
                    }.onFailure {
                        // TODO 예외처리
                    }
            }
        }
    }
