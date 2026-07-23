package junjange.core.data.datasource

import junjange.core.data.model.remote.LottoEntity
import junjange.core.data.model.remote.PensionLotteryHomeEntity

interface LotteryDataSource {
    suspend fun getLotteryRound(): Result<Int>

    suspend fun getPensionLotteryRound(): Result<Int>

    suspend fun getLottoNumber(drwNo: Int): Result<LottoEntity>

    suspend fun getPensionLottoNumber(drwNo: Int): Result<PensionLotteryHomeEntity>
}
