package junjange.core.remote.datasource

import junjange.core.data.datasource.LotteryDataSource
import junjange.core.data.model.remote.LottoEntity
import junjange.core.data.model.remote.PensionLotteryHomeEntity
import junjange.core.remote.api.LotteryService
import junjange.core.remote.model.response.toData

internal class LotteryDataSourceImpl
    constructor(
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
