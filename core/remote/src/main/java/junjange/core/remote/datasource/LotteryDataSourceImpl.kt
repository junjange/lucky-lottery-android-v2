package junjange.core.remote.datasource

import junjange.core.data.datasource.LotteryDataSource
import junjange.core.data.model.remote.LotteryGetEntity
import junjange.core.data.model.remote.LotteryRandomNumbersEntity
import junjange.core.data.model.remote.LottoEntity
import junjange.core.data.model.remote.PensionLotteryHomeEntity
import junjange.core.remote.api.ApiService
import junjange.core.remote.api.LotteryService
import junjange.core.remote.model.request.LotteryRandomRequest
import junjange.core.remote.model.response.toData
import javax.inject.Inject

internal class LotteryDataSourceImpl
    @Inject
    constructor(
        private val apiService: ApiService,
        private val lotteryService: LotteryService,
    ) : LotteryDataSource {
        override suspend fun getLotteryRound(): Result<Int> =
            runCatching {
                val response = lotteryService.getLottoInfo(round = null)
                response.data
                    ?.list
                    ?.firstOrNull()
                    ?.round
                    ?: throw IllegalStateException("Unable to fetch latest lotto 645 round")
            }

        override suspend fun getPensionLotteryRound(): Result<Int> =
            runCatching {
                val response = lotteryService.getPensionLotteryInfo(round = null)
                response.data
                    ?.result
                    ?.firstOrNull()
                    ?.settlementEpisode
                    ?: throw IllegalStateException("Unable to fetch latest pension lottery round")
            }

        override suspend fun getLotteryGet(
            page: Int,
            size: Int,
        ): Result<LotteryGetEntity> =
            runCatching {
                apiService.getLotteryGet(page = page, size = size).data.toData()
            }

        override suspend fun postLotterySave(
            firstNum: Int,
            secondNum: Int,
            thirdNum: Int,
            fourthNum: Int,
            fifthNum: Int,
            sixthNum: Int,
        ): Result<Unit> =
            runCatching {
                val body =
                    LotteryRandomRequest(
                        firstNum = firstNum,
                        secondNum = secondNum,
                        thirdNum = thirdNum,
                        fourthNum = fourthNum,
                        fifthNum = fifthNum,
                        sixthNum = sixthNum,
                    )
                apiService.postLotterySave(body = body)
            }

        override suspend fun getLotteryRandom(): Result<LotteryRandomNumbersEntity> =
            runCatching {
                apiService.getLotteryRandom().data.toData()
            }

        override suspend fun getLottoNumber(drwNo: Int): Result<LottoEntity> =
            runCatching {
                val response = lotteryService.getLottoInfo(round = drwNo)
                response.toData(drwNo) ?: throw IllegalStateException("Lotto 645 information not available for round $drwNo")
            }

        override suspend fun getPensionLottoNumber(drwNo: Int): Result<PensionLotteryHomeEntity> =
            runCatching {
                val response = lotteryService.getPensionLotteryInfo(round = drwNo)
                response.toData(drwNo) ?: throw IllegalStateException("Pension lottery information not available for round $drwNo")
            }
    }
