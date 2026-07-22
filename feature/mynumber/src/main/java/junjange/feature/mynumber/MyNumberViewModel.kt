package junjange.feature.mynumber

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import junjange.core.domain.usecase.DeleteAllLotteryUseCase
import junjange.core.domain.usecase.DeleteAllPensionLotteryUseCase
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
import javax.inject.Inject

@HiltViewModel
class MyNumberViewModel
    @Inject
    constructor(
        private val ocrService: OcrService,
        private val insertLotteryUseCase: InsertLotteryUseCase,
        private val loadLotteryRoundsUseCase: LoadLotteryRoundsUseCase,
        private val insertPensionLotteryUseCase: InsertPensionLotteryUseCase,
        private val loadPensionLotteryRoundsUseCase: LoadPensionLotteryRoundsUseCase,
        private val deleteLotteryByRoundAndIdUseCase: DeleteLotteryByRoundAndIdUseCase,
        private val deletePensionLotteryByRoundAndIdUseCase: DeletePensionLotteryByRoundAndIdUseCase,
        private val deleteAllLotteryUseCase: DeleteAllLotteryUseCase,
        private val deleteAllPensionLotteryUseCase: DeleteAllPensionLotteryUseCase,
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
                is Event.InsertLotteries -> insertLotteries(lotteries = event.lotteries)
                is Event.InsertPensionLotteries -> insertPensionLotteries(pensionLotteries = event.pensionLotteries)
                is Event.LottoTextOfImage -> getLottoTextOfImage(imagePath = event.imagePath)
                is Event.PensionLottoTextOfImage -> getPensionLottoTextOfImage(imagePath = event.imagePath)
                is Event.DeleteLottery -> deleteLottery(userRoundIds = event.userRoundIds)
                is Event.DeletePensionLottery -> deletePensionLottery(userRoundIds = event.userRoundIds)
                is Event.DeleteAllLottery -> deleteAllLottery()
                is Event.DeleteAllPensionLottery -> deleteAllPensionLottery()
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

        private fun insertLotteries(lotteries: List<List<String>>) {
            launch {
                loading(isLoading = true)
                val results =
                    lotteries.map { lottery ->
                        insertLotteryUseCase(
                            firstNum = lottery[0].toInt(),
                            secondNum = lottery[1].toInt(),
                            thirdNum = lottery[2].toInt(),
                            fourthNum = lottery[3].toInt(),
                            fifthNum = lottery[4].toInt(),
                            sixthNum = lottery[5].toInt(),
                        )
                    }
                if (results.any { it.isSuccess }) {
                    _effect.send(Effect.LotteryRefresh)
                }
                if (results.all { it.isSuccess }) {
                    _effect.send(Effect.ShowMessage(MyNumberMessage.LOTTERY_INSERT_SUCCESS))
                } else {
                    _effect.send(Effect.ShowMessage(MyNumberMessage.LOTTERY_INSERT_FAILED))
                }
                loading(false)
            }
        }

        private fun insertPensionLotteries(pensionLotteries: List<List<String>>) {
            launch {
                loading(isLoading = true)
                val results =
                    pensionLotteries.map { pensionLottery ->
                        insertPensionLotteryUseCase(
                            group = pensionLottery[0].toInt(),
                            firstNum = pensionLottery[1].toInt(),
                            secondNum = pensionLottery[2].toInt(),
                            thirdNum = pensionLottery[3].toInt(),
                            fourthNum = pensionLottery[4].toInt(),
                            fifthNum = pensionLottery[5].toInt(),
                            sixthNum = pensionLottery[6].toInt(),
                        )
                    }
                if (results.any { it.isSuccess }) {
                    _effect.send(Effect.PensionLotteryRefresh)
                }
                if (results.all { it.isSuccess }) {
                    _effect.send(Effect.ShowMessage(MyNumberMessage.PENSION_LOTTERY_INSERT_SUCCESS))
                } else {
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
                insertLotteries(lotteries = lottoNumbers)
            }
        }

        private fun getPensionLottoTextOfImage(imagePath: String) {
            val text = ocrService.getTextOfImage(File(imagePath))

            val lottoNumbers = text.extractPensionLottoNumbers()

            if (lottoNumbers.isValidPensionLottoNumbers()) {
                insertPensionLotteries(pensionLotteries = lottoNumbers)
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

        private fun deleteAllLottery() {
            launch {
                deleteAllLotteryUseCase()
                _effect.send(Effect.LotteryRefresh)
            }
        }

        private fun deleteAllPensionLottery() {
            launch {
                deleteAllPensionLotteryUseCase()
                _effect.send(Effect.PensionLotteryRefresh)
            }
        }

        private fun loading(isLoading: Boolean) {
            _state.update { state ->
                state.copy(isLoading = isLoading)
            }
        }
    }
