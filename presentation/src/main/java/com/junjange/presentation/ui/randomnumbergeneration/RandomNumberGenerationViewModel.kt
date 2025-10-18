package com.junjange.presentation.ui.randomnumbergeneration

import androidx.lifecycle.SavedStateHandle
import junjange.core.domain.usecase.GetLotteryRandomUseCase
import junjange.core.domain.usecase.GetPensionLotteryRandomUseCase
import junjange.core.domain.usecase.InsertLotteryUseCase
import junjange.core.domain.usecase.InsertPensionLotteryUseCase
import com.junjange.presentation.ui.randomnumber.RandomNumberMessage.LOTTERY_NUMBER_SAVED
import com.junjange.presentation.ui.randomnumber.RandomNumberMessage.PENSION_LOTTERY_SAVED
import com.junjange.presentation.ui.randomnumbergeneration.RandomNumberGenerationContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import junjange.core.ui.base.BaseViewModel
import junjange.core.ui.component.LottoType

@HiltViewModel
class RandomNumberGenerationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getLotteryRandomUseCase: GetLotteryRandomUseCase,
        private val insertLotteryUseCase: InsertLotteryUseCase,
        private val getPensionLotteryRandomUseCase: GetPensionLotteryRandomUseCase,
        private val insertPensionLotteryUseCase: InsertPensionLotteryUseCase,
    ) : BaseViewModel() {
        private val _state = MutableStateFlow(State())
        val state: StateFlow<State> = _state.asStateFlow()

        private val _effect = Channel<Effect>(Channel.BUFFERED)
        val effect get() = _effect.receiveAsFlow()

        init {
            val lottoType =
                savedStateHandle.get<String>(RandomNumberGenerationActivity.LOTTO_TYPE) ?: finish()

            _state.update { state ->
                state.copy(isLotto645 = lottoType == LottoType.LOTTO645.name)
            }
        }

        fun event(event: Event) {
            when (event) {
                is Event.Back -> finish()
                is Event.GenerateRandomLottery -> generateRandomLottery()
                is Event.GenerateRandomPensionLottery -> generateRandomPensionLottery()
                is Event.SaveLottery -> postLotterySave()
                is Event.SavePensionLottery -> postPensionLotterySave()
            }
        }

        private fun finish() {
            launch {
                _effect.send(Effect.Finish)
            }
        }

        private fun generateRandomLottery() {
            launch {
                repeat(6) {
                    getLotteryRandomUseCase()
                        .onSuccess {
                            _state.update { state ->
                                state.copy(saveIsEnabled = false, lotteryRandomNumbers = it)
                            }
                        }.onFailure {
                            // TODO 예외 처리
                        }

                    delay(500)
                }
                _state.update { state ->
                    state.copy(saveIsEnabled = true)
                }
            }
        }

        private fun generateRandomPensionLottery() {
            launch {
                repeat(6) {
                    getPensionLotteryRandomUseCase()
                        .onSuccess {
                            _state.update { state ->
                                state.copy(saveIsEnabled = false, pensionLotteryRandom = it)
                            }
                        }.onFailure {
                            // TODO 예외 처리
                        }

                    delay(500)
                }
                _state.update { state ->
                    state.copy(saveIsEnabled = true)
                }
            }
        }

        private fun postLotterySave() {
            launch {
                val lotteryNumbers = _state.value.lotteryRandomNumbers ?: return@launch
                insertLotteryUseCase(
                    firstNum = lotteryNumbers.firstNum,
                    secondNum = lotteryNumbers.secondNum,
                    thirdNum = lotteryNumbers.thirdNum,
                    fourthNum = lotteryNumbers.fourthNum,
                    fifthNum = lotteryNumbers.fifthNum,
                    sixthNum = lotteryNumbers.sixthNum,
                ).onSuccess {
                    _effect.send(Effect.ShowMessage(LOTTERY_NUMBER_SAVED))
                }.onFailure {
                    // TODO 예외 처리
                }
            }
        }

        private fun postPensionLotterySave() {
            launch {
                val lotteryNumbers = _state.value.pensionLotteryRandom ?: return@launch
                insertPensionLotteryUseCase(
                    group = lotteryNumbers.pensionGroup,
                    firstNum = lotteryNumbers.pensionFirstNum,
                    secondNum = lotteryNumbers.pensionSecondNum,
                    thirdNum = lotteryNumbers.pensionThirdNum,
                    fourthNum = lotteryNumbers.pensionFourthNum,
                    fifthNum = lotteryNumbers.pensionFifthNum,
                    sixthNum = lotteryNumbers.pensionSixthNum,
                ).onSuccess {
                    _effect.send(Effect.ShowMessage(PENSION_LOTTERY_SAVED))
                }.onFailure {
                    // TODO 예외 처리
                }
            }
        }
    }
