package junjange.core.domain.repository

import junjange.core.domain.model.LotteryGet
import junjange.core.domain.model.LotteryGetContent
import junjange.core.domain.model.LotteryNumbers
import junjange.core.domain.model.LotteryRandomNumbers

interface LotteryRepository {
    suspend fun getLotteryRound(): Result<Int>

    suspend fun getLotteryGet(
        page: Int,
        size: Int,
    ): Result<LotteryGet>

    suspend fun postLotterySave(
        firstNum: Int,
        secondNum: Int,
        thirdNum: Int,
        fourthNum: Int,
        fifthNum: Int,
        sixthNum: Int,
    ): Result<Unit>

    suspend fun getLotteryRandom(): Result<LotteryRandomNumbers>

    suspend fun getLottoNumber(drwNo: Int): Result<LotteryNumbers>

    suspend fun loadLotteryRounds(
        page: Int,
        size: Int,
    ): Result<List<LotteryGetContent>>

    suspend fun insertLottery(
        firstNum: Int,
        secondNum: Int,
        thirdNum: Int,
        fourthNum: Int,
        fifthNum: Int,
        sixthNum: Int,
    ): Result<Unit>

    suspend fun deleteLotteryByRoundAndId(
        round: Int,
        id: Long,
    ): Result<Unit>
}
