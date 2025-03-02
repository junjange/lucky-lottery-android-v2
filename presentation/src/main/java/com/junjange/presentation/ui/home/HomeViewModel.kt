package com.junjange.presentation.ui.home

import com.junjange.domain.usecase.GetLotteryRoundUseCase
import com.junjange.domain.usecase.GetLotteryUseCase
import com.junjange.domain.usecase.GetPensionLotteryRoundUseCase
import com.junjange.domain.usecase.GetPensionLotteryUseCase
import com.junjange.presentation.base.BaseViewModel
import com.junjange.presentation.ui.home.HomeContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getLotteryRoundUseCase: GetLotteryRoundUseCase,
        private val getPensionLotteryRoundUseCase: GetPensionLotteryRoundUseCase,
        private val getLotteryUseCase: GetLotteryUseCase,
        private val getPensionLotteryUseCase: GetPensionLotteryUseCase,
    ) : BaseViewModel() {
        private val _state = MutableStateFlow(State())
        val state: StateFlow<State> = _state.asStateFlow()

        private val _effect = Channel<Effect>(Channel.BUFFERED)
        val effect get() = _effect.receiveAsFlow()

        init {
            launch {
                loading(true)
                val lotteryDeferred = async { fetchLatestLotteryRound() }
                val pensionDeferred = async { fetchLatestPensionLotteryRound() }

                awaitAll(lotteryDeferred, pensionDeferred)
                loading(false)
            }
        }

        fun event(event: Event) {
            when (event) {
                is Event.ChangeLottery -> changeLottery(offset = event.offset)
                is Event.ChangePensionLottery -> changePensionLottery(offset = event.offset)
            }
        }

        private suspend fun fetchLatestLotteryRound() {
            getLotteryRoundUseCase()
                .onSuccess { round ->
                    fetchLotteryNumbers(round)
                }.onFailure { }
        }

        private suspend fun fetchLatestPensionLotteryRound() {
            getPensionLotteryRoundUseCase()
                .onSuccess { round ->
                    fetchPensionLotteryNumbers(round)
                }.onFailure { }
        }

        private suspend fun fetchLotteryNumbers(round: Int) {
            getLotteryUseCase(round)
                .onSuccess { numbers ->
                    _state.update {
                        it.copy(
                            lotteryNumbers = numbers,
                            lotteryRound = round,
                        )
                    }
                }.onFailure {
                    _effect.send(Effect.ShowMessage(HomeMessage.LOTTO_NUMBER_NOT_FOUND))
                }
        }

        private suspend fun fetchPensionLotteryNumbers(round: Int) {
            getPensionLotteryUseCase(round)
                .onSuccess { numbers ->
                    _state.update {
                        it.copy(
                            pensionLotteryHome = numbers,
                            pensionLotteryRound = round,
                        )
                    }
                }.onFailure {
                    _effect.send(Effect.ShowMessage(HomeMessage.PENSION_NUMBER_NOT_FOUND))
                }
        }

        private fun changeLottery(offset: Int) {
            launch {
                val round = state.value.lotteryRound
                fetchLotteryNumbers(round + offset)
            }
        }

        private fun changePensionLottery(offset: Int) {
            launch {
                val round = state.value.pensionLotteryRound
                fetchPensionLotteryNumbers(round + offset)
            }
        }

        private fun loading(isLoading: Boolean) {
            _state.update { state ->
                state.copy(isLoading = isLoading)
            }
        }
    }
