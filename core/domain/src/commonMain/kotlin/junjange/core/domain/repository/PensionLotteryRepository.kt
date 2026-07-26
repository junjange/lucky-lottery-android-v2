package junjange.core.domain.repository

import junjange.core.domain.model.PensionLotteryGetContent
import junjange.core.domain.model.PensionLotteryHome
import junjange.core.domain.model.PensionLotteryRandom

interface PensionLotteryRepository {
    suspend fun getPensionLotteryRandom(): Result<PensionLotteryRandom>

    suspend fun getPensionLotteryRound(): Result<Int>

    suspend fun getPensionLottoNumber(drwNo: Int): Result<PensionLotteryHome>

    suspend fun loadPensionLotteryRounds(
        page: Int,
        size: Int,
    ): Result<List<PensionLotteryGetContent>>

    suspend fun insertPensionLottery(
        group: Int,
        firstNum: Int,
        secondNum: Int,
        thirdNum: Int,
        fourthNum: Int,
        fifthNum: Int,
        sixthNum: Int,
    ): Result<Unit>

    suspend fun deletePensionLotteryByRoundAndId(
        round: Int,
        id: Long,
    ): Result<Unit>

    suspend fun deleteAllPensionLottery(): Result<Unit>
}
