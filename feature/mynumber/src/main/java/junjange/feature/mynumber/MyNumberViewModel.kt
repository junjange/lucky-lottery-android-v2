package junjange.feature.mynumber

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import junjange.core.domain.usecase.DeleteLotteryByRoundAndIdUseCase
import junjange.core.domain.usecase.DeletePensionLotteryByRoundAndIdUseCase
import junjange.core.domain.usecase.InsertLotteryUseCase
import junjange.core.domain.usecase.InsertPensionLotteryUseCase
import junjange.core.domain.usecase.LoadLotteryRoundsUseCase
import junjange.core.domain.usecase.LoadPensionLotteryRoundsUseCase
import junjange.core.ocr.service.OcrService
import junjange.core.ui.base.BaseViewModel
import junjange.feature.mynumber.MyNumberContract.*
import junjange.feature.mynumber.MyNumberContract.Effect.NavigateToGallery
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.io.File


class MyNumberViewModel
    
    constructor(
        private val ocrService: OcrService,
        private val insertLotteryUseCase: InsertLotteryUseCase,
        private val loadLotteryRoundsUseCase: LoadLotteryRoundsUseCase,
        private val insertPensionLotteryUseCase: InsertPensionLotteryUseCase,
        private val loadPensionLotteryRoundsUseCase: LoadPensionLotteryRoundsUseCase,
        private val deleteLotteryByRoundAndIdUseCase: DeleteLotteryByRoundAndIdUseCase,
        private val deletePensionLotteryByRoundAndIdUseCase: DeletePensionLotteryByRoundAndIdUseCase,
    ) : BaseViewModel() {
        private val _state = MutableStateFlow(State())
        val state: StateFlow<State> = _state.asStateFlow()

        private val _effect = Channel<Effect>(Channel.BUFFERED)
        val effect get() = _effect.receiveAsFlow()

        init {
            loadLottery()
            loadPensionLottery()
        }

        fun event(event: Event) {
            when (event) {
                is Event.PickedImage -> onPickedImage()
                is Event.LoadLottery -> loadLottery()
                is Event.LoadPensionLottery -> loadPensionLottery()
                is Event.InsertLottery -> insertLottery(lottery = event.lottery)
                is Event.InsertPensionLottery -> insertPensionLottery(pensionLottery = event.pensionLottery)
                is Event.LottoTextOfImage -> getLottoTextOfImage(imagePath = event.imagePath)
                is Event.PensionLottoTextOfImage -> getPensionLottoTextOfImage(imagePath = event.imagePath)
                is Event.DeleteLottery -> deleteLottery(userRoundIds = event.userRoundIds)
                is Event.DeletePensionLottery -> deletePensionLottery(userRoundIds = event.userRoundIds)
                is Event.ShowDialog -> showDialog(isDialogShowing = event.isDialogShowing)
            }
        }

        private fun showDialog(isDialogShowing: Boolean) {
            launch {
                _state.update {
                    state.value.copy(
                        isDeleteLotteryDialogShowing = isDialogShowing,
                    )
                }
            }
        }

        private fun loadLottery() {
            launch {
                val lotteryFlow =
                    createLotteryPagingSource(loadLotteryRoundsUseCase = loadLotteryRoundsUseCase).flow.cachedIn(
                        viewModelScope,
                    )

                _state.update {
                    state.value.copy(lotteryFlow = lotteryFlow)
                }
            }
        }

        private fun loadPensionLottery() {
            launch {
                val pensionLotteryFlow =
                    createPensionLotteryPagingSource(loadPensionLotteryRoundsUseCase = loadPensionLotteryRoundsUseCase).flow.cachedIn(
                        viewModelScope,
                    )

                _state.update {
                    state.value.copy(pensionLotteryFlow = pensionLotteryFlow)
                }
            }
        }

        private fun insertLottery(lottery: List<String>) {
            launch {
                loading(isLoading = true)
                insertLotteryUseCase(
                    firstNum = lottery[0].toInt(),
                    secondNum = lottery[1].toInt(),
                    thirdNum = lottery[2].toInt(),
                    fourthNum = lottery[3].toInt(),
                    fifthNum = lottery[4].toInt(),
                    sixthNum = lottery[5].toInt(),
                ).onSuccess {
                    _effect.send(Effect.LotteryRefresh)
                    _effect.send(Effect.ShowMessage(MyNumberMessage.LOTTERY_INSERT_SUCCESS))
                }.onFailure {
                    _effect.send(Effect.ShowMessage(MyNumberMessage.LOTTERY_INSERT_FAILED))
                }
                loading(false)
            }
        }

        private fun insertPensionLottery(pensionLottery: List<String>) {
            launch {
                loading(isLoading = true)
                insertPensionLotteryUseCase(
                    group = pensionLottery[0].toInt(),
                    firstNum = pensionLottery[1].toInt(),
                    secondNum = pensionLottery[2].toInt(),
                    thirdNum = pensionLottery[3].toInt(),
                    fourthNum = pensionLottery[4].toInt(),
                    fifthNum = pensionLottery[5].toInt(),
                    sixthNum = pensionLottery[6].toInt(),
                ).onSuccess {
                    _effect.send(Effect.PensionLotteryRefresh)
                    _effect.send(Effect.ShowMessage(MyNumberMessage.PENSION_LOTTERY_INSERT_SUCCESS))
                }.onFailure {
                    _effect.send(Effect.ShowMessage(MyNumberMessage.PENSION_LOTTERY_INSERT_FAILED))
                }
                loading(false)
            }
        }

        private fun onPickedImage() {
            launch {
                _effect.send(NavigateToGallery)
            }
        }

        private fun getLottoTextOfImage(imagePath: String) {
            val text = ocrService.getTextOfImage(File(imagePath))

            val lottoNumbers = text.extractLottoNumbers()

            if (lottoNumbers.isValidLottoNumbers()) {
                lottoNumbers.forEach { lottoNumber ->
                    insertLottery(lottery = lottoNumber)
                }
            }
        }

        private fun getPensionLottoTextOfImage(imagePath: String) {
            val text = ocrService.getTextOfImage(File(imagePath))

            val lottoNumbers = text.extractPensionLottoNumbers()

            if (lottoNumbers.isValidPensionLottoNumbers()) {
                lottoNumbers.forEach { lottoNumber ->
                    insertPensionLottery(pensionLottery = lottoNumber)
                }
            }
        }

        private fun deleteLottery(userRoundIds: List<UserRoundId>) {
            launch {
                userRoundIds.forEach { (round, id) ->
                    deleteLotteryByRoundAndIdUseCase(round = round, id = id)
                }
                _effect.send(Effect.LotteryRefresh)
            }
        }

        private fun deletePensionLottery(userRoundIds: List<UserRoundId>) {
            launch {
                userRoundIds.forEach { (round, id) ->
                    deletePensionLotteryByRoundAndIdUseCase(round = round, id = id)
                }
                _effect.send(Effect.PensionLotteryRefresh)
            }
        }

        private fun loading(isLoading: Boolean) {
            _state.update { state ->
                state.copy(isLoading = isLoading)
            }
        }
    }
