package junjange.feature.home

import junjange.core.domain.usecase.GetLotteryRoundUseCase
import junjange.core.domain.usecase.GetLotteryUseCase
import junjange.core.domain.usecase.GetPensionLotteryRoundUseCase
import junjange.core.domain.usecase.GetPensionLotteryUseCase
import junjange.core.ui.base.BaseViewModel
import junjange.feature.home.HomeContract.Effect
import junjange.feature.home.HomeContract.Event
import junjange.feature.home.HomeContract.State
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
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
            refreshLottery()
        }

        fun event(event: Event) {
            when (event) {
                is Event.Refresh -> refreshLottery()
                is Event.ChangeLottery -> changeLottery(offset = event.offset)
                is Event.ChangePensionLottery -> changePensionLottery(offset = event.offset)
            }
        }

        private fun refreshLottery() {
            launch {
                loading(true)
                setError(false)
                val lotteryDeferred = async { fetchLatestLotteryRound() }
                val pensionDeferred = async { fetchLatestPensionLotteryRound() }

                val results = awaitAll(lotteryDeferred, pensionDeferred)
                val hasError = results.all { !it }

                loading(false)
                setError(hasError)
            }
        }

        private suspend fun fetchLatestLotteryRound(): Boolean =
            getLotteryRoundUseCase()
                .onSuccess { round ->
                    fetchLotteryNumbers(round)
                }.isSuccess

        private suspend fun fetchLatestPensionLotteryRound(): Boolean =
            getPensionLotteryRoundUseCase()
                .onSuccess { round ->
                    fetchPensionLotteryNumbers(round)
                }.isSuccess

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

        private fun setError(isError: Boolean) {
            _state.update { state ->
                state.copy(isError = isError)
            }
        }
    }
