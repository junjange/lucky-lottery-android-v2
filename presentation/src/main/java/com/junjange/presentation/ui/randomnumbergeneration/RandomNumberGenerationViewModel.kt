package com.junjange.presentation.ui.randomnumbergeneration

import androidx.lifecycle.SavedStateHandle
import com.junjange.domain.usecase.GetLotteryRandomUseCase
import com.junjange.domain.usecase.GetPensionLotteryRandomUseCase
import com.junjange.domain.usecase.PostLotterySaveUseCase
import com.junjange.domain.usecase.PostPensionLotterySaveUseCase
import com.junjange.presentation.base.BaseViewModel
import com.junjange.presentation.component.LottoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RandomNumberGenerationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getLotteryRandomUseCase: GetLotteryRandomUseCase,
        private val postLotterySaveUseCase: PostLotterySaveUseCase,
        private val getPensionLotteryRandomUseCase: GetPensionLotteryRandomUseCase,
        private val postPensionLotterySaveUseCase: PostPensionLotterySaveUseCase,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(RandomNumberGenerationState())
        val uiState: StateFlow<RandomNumberGenerationState> = _uiState.asStateFlow()

        init {
            savedStateHandle.get<String>(RandomNumberGenerationActivity.LOTTO_TYPE)?.let { lottoType ->
                _uiState.update { state ->
                    state.copy(isLotto645 = lottoType == LottoType.LOTTO645.name)
                }
            } ?: run {
                // TODO : 예외 처리
            }
        }

        fun generate645RandomNumbers() {
            launch {
                repeat(6) {
                    getLotteryRandomUseCase()
                        .onSuccess {
                            _uiState.update { state ->
                                state.copy(saveIsEnabled = false, lotteryRandomNumbers = it)
                            }
                        }.onFailure {
                            // TODO 예외 처리
                        }

                    delay(500)
                }
                _uiState.update { state ->
                    state.copy(saveIsEnabled = true)
                }
            }
        }

        fun generate720RandomNumbers() {
            launch {
                repeat(6) {
                    getPensionLotteryRandomUseCase()
                        .onSuccess {
                            _uiState.update { state ->
                                state.copy(saveIsEnabled = false, pensionLotteryRandom = it)
                            }
                        }.onFailure {
                            // TODO 예외 처리
                        }

                    delay(500)
                }
                _uiState.update { state ->
                    state.copy(saveIsEnabled = true)
                }
            }
        }

        fun postLotterySave() {
            launch {
                val lotteryNumbers = _uiState.value.lotteryRandomNumbers
                lotteryNumbers?.let {
                    postLotterySaveUseCase(
                        firstNum = it.firstNum,
                        secondNum = it.secondNum,
                        thirdNum = it.thirdNum,
                        fourthNum = it.fourthNum,
                        fifthNum = it.fifthNum,
                        sixthNum = it.sixthNum,
                    ).onSuccess { }.onFailure {
                        // TODO 예외 처리
                    }
                } ?: run {
                    // TODO 예외 처리
                }
            }
        }

        fun postPensionLotterySave() {
            launch {
                val lotteryNumbers = _uiState.value.pensionLotteryRandom
                lotteryNumbers?.let {
                    postPensionLotterySaveUseCase(
                        pensionGroup = it.pensionGroup,
                        pensionFirstNum = it.pensionFirstNum,
                        pensionSecondNum = it.pensionSecondNum,
                        pensionThirdNum = it.pensionThirdNum,
                        pensionFourthNum = it.pensionFourthNum,
                        pensionFifthNum = it.pensionFifthNum,
                        pensionSixthNum = it.pensionSixthNum,
                    ).onSuccess { }.onFailure {
                        // TODO 예외 처리
                    }
                } ?: run {
                    // TODO 예외 처리
                }
            }
        }
    }
