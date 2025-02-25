package com.junjange.presentation.ui.mynumber

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.junjange.domain.usecase.GetLotteryGetUseCase
import com.junjange.domain.usecase.GetPensionLotteryGetUseCase
import com.junjange.domain.usecase.InsertLotteryUseCase
import com.junjange.domain.usecase.LoadLotteryRoundsUseCase
import com.junjange.domain.usecase.PostLotterySaveUseCase
import com.junjange.domain.usecase.PostPensionLotterySaveUseCase
import com.junjange.presentation.base.BaseViewModel
import com.junjange.presentation.feature.ocr.OcrService
import com.junjange.presentation.ui.mynumber.MyNumberEffect.NavigateToGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyNumberViewModel
    @Inject
    constructor(
        private val ocrService: OcrService,
        private val getPensionLotteryGetUseCase: GetPensionLotteryGetUseCase,
        private val getLotteryGetUseCase: GetLotteryGetUseCase,
        private val postLotterySaveUseCase: PostLotterySaveUseCase,
        private val postPensionLotterySaveUseCase: PostPensionLotterySaveUseCase,
        private val insertLotteryUseCase: InsertLotteryUseCase,
        private val loadLotteryRoundsUseCase: LoadLotteryRoundsUseCase,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(MyNumberState())
        val uiState: StateFlow<MyNumberState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<MyNumberEffect>()
        val effect: SharedFlow<MyNumberEffect> = _effect.asSharedFlow()

        init {
            getLotteryGet()
            getPensionLotteryGet()
        }

        fun getLotteryGet() {
            loading(isLoading = true)
            uiState.value.lotteryGetContent =
                createLotteryPagingSource(loadLotteryRoundsUseCase = loadLotteryRoundsUseCase).flow.cachedIn(
                    viewModelScope,
                )
            loading(isLoading = false)
        }

        fun getPensionLotteryGet() {
            loading(isLoading = true)
            uiState.value.pensionLotteryGetContent =
                createPensionLotteryPagingSource(getPensionLotteryGetUseCase = getPensionLotteryGetUseCase).flow.cachedIn(
                    viewModelScope,
                )
            loading(isLoading = false)
        }

        private fun loading(isLoading: Boolean) {
            _uiState.update { state ->
                state.copy(isLoading = isLoading)
            }
        }

        private fun postLotterySave(
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ) {
            launch {
                loading(isLoading = true)
                insertLotteryUseCase(
                    firstNum = firstNum,
                    secondNum = secondNum,
                    thirdNum = thirdNum,
                    fourthNum = fourthNum,
                    fifthNum = fifthNum,
                    sixthNum = sixthNum,
                ).onSuccess {
                    loading(false)
//                    getLotteryGet()
                    Log.d("ttt insertLotteryUseCase onSuccess", it.toString())
                }.onFailure {
                    // TODO 예외처리
                    Log.d("ttt insertLotteryUseCase onFailure", it.toString())
                }
            }
        }

        private fun postPensionLotterySave(
            pensionGroup: Int,
            pensionFirstNum: Int,
            pensionSecondNum: Int,
            pensionThirdNum: Int,
            pensionFourthNum: Int,
            pensionFifthNum: Int,
            pensionSixthNum: Int,
        ) {
            launch {
                loading(isLoading = true)
                postPensionLotterySaveUseCase(
                    pensionGroup = pensionGroup,
                    pensionFirstNum = pensionFirstNum,
                    pensionSecondNum = pensionSecondNum,
                    pensionThirdNum = pensionThirdNum,
                    pensionFourthNum = pensionFourthNum,
                    pensionFifthNum = pensionFifthNum,
                    pensionSixthNum = pensionSixthNum,
                ).onSuccess {
                    loading(false)
                    getPensionLotteryGet()
                }.onFailure {
                    // TODO 예외처리
                }
            }
        }

        fun onPickedImage() {
            launch {
                _effect.emit(NavigateToGallery)
            }
        }

        fun getLottoTextOfImage(imagePath: String) {
            val text = ocrService.getTextOfImage(File(imagePath))

            val lottoNumbers = text.extractLottoNumbers()

            if (lottoNumbers.isValidLottoNumbers()) {
                lottoNumbers.forEach { lottoNumber ->
                    postLotterySave(
                        lottoNumber[0].toInt(),
                        lottoNumber[1].toInt(),
                        lottoNumber[2].toInt(),
                        lottoNumber[3].toInt(),
                        lottoNumber[4].toInt(),
                        lottoNumber[5].toInt(),
                    )
                }
            }
        }

        fun getPensionLottoTextOfImage(imagePath: String) {
            val text = ocrService.getTextOfImage(File(imagePath))

            val lottoNumbers = text.extractPensionLottoNumbers()

            if (lottoNumbers.isValidPensionLottoNumbers()) {
                lottoNumbers.forEach { lottoNumber ->
                    postPensionLotterySave(
                        lottoNumber[0].toInt(),
                        lottoNumber[1].toInt(),
                        lottoNumber[2].toInt(),
                        lottoNumber[3].toInt(),
                        lottoNumber[4].toInt(),
                        lottoNumber[5].toInt(),
                        lottoNumber[6].toInt(),
                    )
                }
            }
        }
    }

private fun String.extractLottoNumbers(): List<List<String>> = this.split("\n").map { it.split(" ") }

private fun String.extractPensionLottoNumbers(): List<List<String>> =
    this.split("\n").map {
        it.replace(" ", "").replace("조", "").map { it.toString() }
    }

private fun List<List<String>>.isValidLottoNumbers(): Boolean =
    all { lottoNumbers ->
        lottoNumbers.size == 6 &&
            lottoNumbers.all { lottoNumber ->
                lottoNumber.toIntOrNull() != null && lottoNumber.toInt() in 1..45
            }
    }

private fun List<List<String>>.isValidPensionLottoNumbers(): Boolean =
    all { lottoNumbers ->
        lottoNumbers.size == 7 &&
            lottoNumbers.all { lottoNumber ->
                lottoNumber.toIntOrNull() != null && lottoNumber.toInt() in 0..9
            }
    }
