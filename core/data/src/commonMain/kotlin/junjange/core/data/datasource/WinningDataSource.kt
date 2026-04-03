package junjange.core.data.datasource

import junjange.core.data.model.remote.LotteryNumbersEntity
import junjange.core.data.model.remote.PensionLotteryHomeEntity

interface WinningDataSource {
    suspend fun getWinningLotteryHome(): Result<LotteryNumbersEntity>

    suspend fun getWinningPensionLotteryHome(): Result<PensionLotteryHomeEntity>
}
