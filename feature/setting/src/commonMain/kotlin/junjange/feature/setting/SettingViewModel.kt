package junjange.feature.setting

import junjange.core.ui.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow


class SettingViewModel

    constructor() : BaseViewModel() {
        private val _uiState = MutableStateFlow(SettingState())
        val uiState: StateFlow<SettingState> = _uiState.asStateFlow()

        private val _effect = Channel<SettingEffect>(Channel.BUFFERED)
        val effect get() = _effect.receiveAsFlow()

        fun onClickedNotification() {
            launch {
                _effect.send(
                    SettingEffect.NavigateToNotification(
                        lottoNotificationState = uiState.value.lotteryNotificationStatus,
                        pensionLottoNotificationState = uiState.value.pensionLotteryNotificationStatus,
                    ),
                )
            }
        }

        fun onClickedReview() {
            launch {
                _effect.send(SettingEffect.NavigateToReview)
            }
        }

        fun onClickedUsageTerm() {
            launch {
                _effect.send(SettingEffect.NavigateToUsageTerm)
            }
        }
    }
