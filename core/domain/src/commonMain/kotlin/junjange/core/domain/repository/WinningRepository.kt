package junjange.core.domain.repository

import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.PensionLotteryHome

interface WinningRepository {
    suspend fun getLotteryHome(): Result<LotteryNumbers>

    suspend fun getPensionLotteryHome(): Result<PensionLotteryHome>
}
