package junjange.feature.mynumber

import junjange.core.domain.model.LotteryGetContent
import junjange.core.domain.model.PensionLotteryGetContent
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

class MyNumberViewModel
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

        private var lotteryPage = 0
        private var pensionLotteryPage = 0

        init {
            loadLottery(refresh = true)
            loadPensionLottery(refresh = true)
        }

        fun event(event: Event) {
            when (event) {
                is Event.PickedImage -> onPickedImage()
                is Event.RefreshLottery -> loadLottery(refresh = true)
                is Event.RefreshPensionLottery -> loadPensionLottery(refresh = true)
                is Event.LoadMoreLottery -> loadLottery(refresh = false)
                is Event.LoadMorePensionLottery -> loadPensionLottery(refresh = false)
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
            _state.update { it.copy(isDeleteLotteryDialogShowing = isDialogShowing) }
        }

        private fun loadLottery(refresh: Boolean) {
            val current = state.value.lottery
            if (!refresh && (current.loadState == PageLoadState.Loading || current.endReached)) return

            launch {
                if (refresh) lotteryPage = 0
                _state.update {
                    it.copy(
                        lottery =
                            it.lottery.copy(
                                loadState = PageLoadState.Loading,
                                isRefreshing = refresh,
                                endReached = if (refresh) false else it.lottery.endReached,
                            ),
                    )
                }

                loadLotteryRoundsUseCase(page = lotteryPage, size = PAGE_SIZE)
                    .onSuccess { loaded ->
                        lotteryPage++
                        _state.update {
                            val items = if (refresh) loaded else it.lottery.items + loaded
                            it.copy(
                                lottery =
                                    PagedContent(
                                        items = items,
                                        loadState = PageLoadState.Idle,
                                        endReached = loaded.size < PAGE_SIZE,
                                        isRefreshing = false,
                                    ),
                            )
                        }
                    }.onFailure {
                        _state.update {
                            it.copy(
                                lottery =
                                    it.lottery.copy(
                                        loadState = PageLoadState.Error,
                                        isRefreshing = false,
                                    ),
                            )
                        }
                    }
            }
        }

        private fun loadPensionLottery(refresh: Boolean) {
            val current = state.value.pensionLottery
            if (!refresh && (current.loadState == PageLoadState.Loading || current.endReached)) return

            launch {
                if (refresh) pensionLotteryPage = 0
                _state.update {
                    it.copy(
                        pensionLottery =
                            it.pensionLottery.copy(
                                loadState = PageLoadState.Loading,
                                isRefreshing = refresh,
                                endReached = if (refresh) false else it.pensionLottery.endReached,
                            ),
                    )
                }

                loadPensionLotteryRoundsUseCase(page = pensionLotteryPage, size = PAGE_SIZE)
                    .onSuccess { loaded ->
                        pensionLotteryPage++
                        _state.update {
                            val items = if (refresh) loaded else it.pensionLottery.items + loaded
                            it.copy(
                                pensionLottery =
                                    PagedContent(
                                        items = items,
                                        loadState = PageLoadState.Idle,
                                        endReached = loaded.size < PAGE_SIZE,
                                        isRefreshing = false,
                                    ),
                            )
                        }
                    }.onFailure {
                        _state.update {
                            it.copy(
                                pensionLottery =
                                    it.pensionLottery.copy(
                                        loadState = PageLoadState.Error,
                                        isRefreshing = false,
                                    ),
                            )
                        }
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
                    loadLottery(refresh = true)
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
                    loadPensionLottery(refresh = true)
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
            val text = ocrService.getTextOfImage(imagePath)

            val lottoNumbers = text.extractLottoNumbers()

            if (lottoNumbers.isValidLottoNumbers()) {
                insertLotteries(lotteries = lottoNumbers)
            }
        }

        private fun getPensionLottoTextOfImage(imagePath: String) {
            val text = ocrService.getTextOfImage(imagePath)

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
                loadLottery(refresh = true)
            }
        }

        private fun deletePensionLottery(userRoundIds: List<UserRoundId>) {
            launch {
                userRoundIds.forEach { (round, id) ->
                    deletePensionLotteryByRoundAndIdUseCase(round = round, id = id)
                }
                loadPensionLottery(refresh = true)
            }
        }

        private fun deleteAllLottery() {
            launch {
                deleteAllLotteryUseCase()
                loadLottery(refresh = true)
            }
        }

        private fun deleteAllPensionLottery() {
            launch {
                deleteAllPensionLotteryUseCase()
                loadPensionLottery(refresh = true)
            }
        }

        private fun loading(isLoading: Boolean) {
            _state.update { state ->
                state.copy(isLoading = isLoading)
            }
        }

        companion object {
            private const val PAGE_SIZE = 10
        }
    }
